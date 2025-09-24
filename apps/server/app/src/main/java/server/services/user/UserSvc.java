package server.services.user;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import server.models.User;

@Service
@Transactional
public class UserSvc {

    private final UserRepo repo;

    public UserSvc(UserRepo repo) {
        this.repo = repo;
    }

    public Mono<User> createUser(User u) {
        return repo.save(u)
                .flatMap(saved -> repo.findById(saved.getId()));
    }

    public Mono<User> findByEmail(String email) {
        return repo.findUserByEmail(email);
    }

    public Mono<User> softDelete(UUID id) {
        return repo.findById(id)
                .flatMap(user -> {
                    user.setDeletedAt(System.currentTimeMillis());
                    return repo.save(user);
                });
    }

    public Mono<Integer> hardDelete(UUID id) {
        return repo.findById(id)
                .flatMap(user -> repo.delete(user)
                        .thenReturn(1)
                        .doOnSuccess(v -> System.out.println("🔪 deleted 1 column")))
                .switchIfEmpty(Mono.defer(() -> {
                    System.out.println("🗑️ deleted 0 columns");
                    return Mono.just(0);
                }));
    }

    public Flux<User> findAll() {
        AtomicInteger counter = new AtomicInteger(0);
        return repo.findAll()
                .doOnNext(u -> counter.incrementAndGet())
                .doOnComplete(() -> System.out.println("Total users: " + counter.get()));
    }

}
