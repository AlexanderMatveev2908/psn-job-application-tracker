package server.services.user;

import java.util.UUID;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Mono;
import server.models.User;

@Repository
public interface UserRepo extends ReactiveCrudRepository<User, UUID> {

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    Mono<User> findUserByEmail(String email);

}
