package server.models.user.svc;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple3;
import reactor.util.function.Tuples;
import server.conf.mail.MailSvc;
import server.conf.mail.etc.SubjEmailT;
import server.decorators.flow.ErrAPI;
import server.lib.security.cbc_hmac.CbcHmac;
import server.lib.security.cbc_hmac.etc.RecCreateCbcHmac;
import server.lib.security.session_tokens.RecSessionTokens;
import server.lib.security.session_tokens.SessionManager;
import server.models.applications.JobAppl;
import server.models.applications.svc.JobApplSvc;
import server.models.backup_code.BkpCodes;
import server.models.backup_code.svc.BkpCodesRepo;
import server.models.token.MyToken;
import server.models.token.etc.AlgT;
import server.models.token.etc.TokenT;
import server.models.token.svc.TokenRepo;
import server.models.user.User;
import server.models.user.etc.UserPop;

@Service
@Transactional
@RequiredArgsConstructor
@SuppressFBWarnings({ "EI2" })
public class UserSvc {

    private final UserRepo userRepo;
    private final TokenRepo tokensRepo;
    private final BkpCodesRepo bkpCodesRepo;
    private final JobApplSvc jobApplSvc;
    private final SessionManager sessionMng;
    private final MailSvc mailSvc;
    private final CbcHmac cbcHmac;

    public Mono<Tuple3<User, MyToken, String>> insert(User us) {
        return findByEmail(us.getEmail())
                .flatMap(existing -> Mono.<Tuple3<User, MyToken, String>>error(
                        new ErrAPI("an account with this email already exists", 409)))
                .switchIfEmpty(Mono.defer(() -> userRepo.insert(us)
                        .flatMap(dbUser -> {
                            RecSessionTokens recSession = sessionMng.genSessionTokens(dbUser.getId());

                            MyToken refreshTk = new MyToken(
                                    dbUser.getId(),
                                    AlgT.RSA_OAEP256_A256GCM,
                                    TokenT.REFRESH,
                                    recSession.recJwe());

                            RecCreateCbcHmac recCreateCbcHmac = cbcHmac.create(
                                    AlgT.AES_CBC_HMAC_SHA256,
                                    TokenT.CONF_EMAIL,
                                    dbUser.getId());

                            return Mono.zip(
                                    tokensRepo.insert(refreshTk),
                                    tokensRepo.insert(recCreateCbcHmac.token())).flatMap(tpl -> {
                                        MyToken dbToken = tpl.getT1();

                                        return mailSvc.sendRctHtmlMail(
                                                SubjEmailT.CONFIRM_EMAIL,
                                                dbUser,
                                                recCreateCbcHmac.clientToken())
                                                .thenReturn(Tuples.of(dbUser, dbToken, recSession.jwt()));
                                    });
                        })));
    }

    public Mono<User> findByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    public Mono<User> findById(UUID id) {
        return userRepo.findById(id);
    }

    public Mono<UserPop> getUserPop(UUID userId) {
        Mono<User> userMono = userRepo.findById(userId);
        Mono<List<MyToken>> tokensMono = tokensRepo.findByUserId(userId).collectList();
        Mono<List<BkpCodes>> codesMono = bkpCodesRepo.findByUserId(userId).collectList();
        Mono<List<JobAppl>> applMono = jobApplSvc.findByUserId(userId).collectList();

        return Mono.zip(userMono, tokensMono, codesMono, applMono)
                .map(tuple -> new UserPop(tuple.getT1(), tuple.getT2(), tuple.getT3(), tuple.getT4()));
    }

    public Mono<User> softDelete(UUID id) {
        return userRepo.findById(id)
                .flatMap(user -> {
                    user.setDeletedAt(System.currentTimeMillis());
                    return userRepo.save(user);
                });
    }

    public Mono<Integer> hardDelete(UUID id) {
        return userRepo.findById(id)
                .flatMap(user -> userRepo.delete(user)
                        .thenReturn(1)
                        .doOnSuccess(v -> System.out.println("🔪 deleted 1 column")))
                .switchIfEmpty(Mono.defer(() -> {
                    System.out.println("🗑️ deleted 0 columns");
                    return Mono.just(0);
                }));
    }

    public Flux<User> findAll() {
        AtomicInteger counter = new AtomicInteger(0);
        return userRepo.findAll()
                .doOnNext(u -> counter.incrementAndGet())
                .doOnComplete(() -> System.out.println("Total users: " + counter.get()));
    }

}
