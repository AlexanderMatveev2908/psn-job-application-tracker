package server.services.user;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Mono;
import server.models.User;

@Repository
public interface UserRepo extends ReactiveCrudRepository<User, String> {

    @Query("SELECT * FROM users WHERE email = ?1")
    Mono<User> findUserByEmail(String email);
}
