package server.services.user;

import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import server.models.User;

@Service
public class UserSvc {

    private final UserRepo repo;

    public UserSvc(UserRepo repo) {
        this.repo = repo;
    }

    public Mono<User> createUser(User u) {
        return repo.save(u);
    }

    public Flux<User> findAll() {
        return repo.findAll();
    }
}
