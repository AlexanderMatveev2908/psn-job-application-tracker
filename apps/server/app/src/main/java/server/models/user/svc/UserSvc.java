package server.models.user.svc;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import server.models.token.MyToken;
import server.models.token.side.svc.TokenRepo;
import server.models.user.User;
import server.models.user.side.UserPop;

@Service
@Transactional
@SuppressFBWarnings({ "EI2" })
public class UserSvc {

    private final UserRepo userRepo;
    private final TokenRepo tokensRepo;

    public UserSvc(UserRepo userRepo, TokenRepo tokensRepo) {
        this.userRepo = userRepo;
        this.tokensRepo = tokensRepo;
    }

    public Mono<User> createUser(User u) {
        return userRepo.save(u)
                .flatMap(saved -> userRepo.findById(saved.getId()));
    }

    public Mono<User> findByEmail(String email) {
        return userRepo.findUserByEmail(email);
    }

    public Mono<User> findById(UUID id) {
        return userRepo.findById(id);
    }

    public Mono<UserPop> getUserPop(UUID userId) {
        Mono<User> userMono = userRepo.findById(userId);
        Mono<List<MyToken>> tokensMono = tokensRepo.findByUserId(userId).collectList();

        return Mono.zip(userMono, tokensMono)
                .map(tuple -> new UserPop(tuple.getT1(), tuple.getT2()));
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
