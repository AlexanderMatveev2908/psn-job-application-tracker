package server.features.auth.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;
import server.conf.mail.MailSvc;
import server.decorators.flow.ErrAPI;
import server.lib.security.mng_tokens.TkMng;
import server.lib.security.mng_tokens.etc.RecSessionTokensReturnT;
import server.lib.security.mng_tokens.tokens.cbc_hmac.etc.RecCreateCbcHmacReturnT;
import server.models.token.etc.TokenT;
import server.models.token.svc.TokenRepo;
import server.models.user.User;
import server.models.user.svc.UserRepo;

@Service @Transactional @RequiredArgsConstructor @SuppressFBWarnings({ "EI2" })
public class RegisterSvc {
  private final UserRepo userRepo;
  private final TokenRepo tokensRepo;
  private final TkMng tkMng;
  private final MailSvc mailSvc;

  public Mono<Tuple2<String, String>> register(User us) {
    return userRepo.findByEmail(us.getEmail()).flatMap(
        existing -> Mono.<Tuple2<String, String>>error(new ErrAPI("an account with this email already exists", 409)))
        .switchIfEmpty(userRepo.insert(us).flatMap(dbUser -> {
          RecSessionTokensReturnT recSession = tkMng.genSessionTokens(dbUser.getId());
          RecCreateCbcHmacReturnT recCbcHmac = tkMng.genCbcHmac(TokenT.CONF_EMAIL, dbUser.getId());

          return Mono.when(tokensRepo.insert(recSession.jwe().inst()), tokensRepo.insertWithId(recCbcHmac.inst()))
              .then(mailSvc.sendRctHtmlMail(TokenT.CONF_EMAIL, dbUser, recCbcHmac.clientToken()))
              .thenReturn(Tuples.of(recSession.jwe().clientToken(), recSession.jwt()));
        }));
  }

}
