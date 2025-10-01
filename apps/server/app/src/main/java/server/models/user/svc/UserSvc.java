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
import server.models.applications.JobAppl;
import server.models.applications.svc.JobApplRepo;
import server.models.backup_code.BkpCodes;
import server.models.backup_code.svc.BkpCodesRepo;
import server.models.token.MyToken;
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
    private final JobApplRepo jobApplRepo;

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
        Mono<List<JobAppl>> applMono = jobApplRepo.findByUserId(userId).collectList();

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
