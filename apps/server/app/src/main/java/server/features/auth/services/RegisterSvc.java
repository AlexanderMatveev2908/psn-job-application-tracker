package server.features.auth.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple3;
import reactor.util.function.Tuples;
import server.conf.mail.MailSvc;
import server.conf.mail.etc.SubjEmailT;
import server.decorators.flow.ErrAPI;
import server.lib.security.mng_tokens.MyTkMng;
import server.lib.security.mng_tokens.etc.RecSessionTokensReturnT;
import server.lib.security.mng_tokens.tokens.cbc_hmac.etc.RecCreateCbcHmacReturnT;
import server.models.token.MyToken;
import server.models.token.etc.AlgT;
import server.models.token.etc.TokenT;
import server.models.token.svc.TokenRepo;
import server.models.user.User;
import server.models.user.svc.UserRepo;

@Service
@Transactional
@RequiredArgsConstructor
@SuppressFBWarnings({ "EI2" })
public class RegisterSvc {
        private final UserRepo userRepo;
        private final TokenRepo tokensRepo;
        private final MyTkMng tkMng;
        private final MailSvc mailSvc;

        public Mono<Tuple3<User, MyToken, String>> register(User us) {
                return userRepo.findByEmail(us.getEmail())
                                .flatMap(existing -> Mono.<Tuple3<User, MyToken, String>>error(
                                                new ErrAPI("an account with this email already exists", 409)))
                                .switchIfEmpty(userRepo.insert(us).flatMap(dbUser -> {
                                        RecSessionTokensReturnT recSession = tkMng.genSessionTokens(dbUser.getId());

                                        MyToken refreshTk = new MyToken(dbUser.getId(), AlgT.RSA_OAEP256_A256GCM,
                                                        TokenT.REFRESH, recSession.recJwe());

                                        RecCreateCbcHmacReturnT recCreateCbcHmac = tkMng.genCbcHmac(TokenT.CONF_EMAIL,
                                                        dbUser.getId());

                                        return Mono.zip(tokensRepo.insert(refreshTk),
                                                        tokensRepo.insertWithId(recCreateCbcHmac.token()))
                                                        .flatMap(tpl -> {
                                                                MyToken dbToken = tpl.getT1();

                                                                return mailSvc.sendRctHtmlMail(SubjEmailT.CONFIRM_EMAIL,
                                                                                dbUser, recCreateCbcHmac.clientToken())
                                                                                .thenReturn(Tuples.of(dbUser, dbToken,
                                                                                                recSession.jwt()));
                                                        });
                                }));
        }

}
