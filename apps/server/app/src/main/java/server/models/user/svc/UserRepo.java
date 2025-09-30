package server.models.user.svc;

import java.util.UUID;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Mono;
import server.models.user.User;

@Repository
public interface UserRepo extends ReactiveCrudRepository<User, UUID> {

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    Mono<User> findByEmail(String email);

    @Query("""
                INSERT INTO users (first_name, last_name, email, password)
                VALUES (:#{#user.firstName}, :#{#user.lastName}, :#{#user.email}, :#{#user.password})
                RETURNING *
            """)
    Mono<User> insert(User user);

    // @Query("""
    // SELECT
    // us.*,
    // COALESCE(
    // (
    // SELECT json_agg(to_jsonb(t))
    // FROM tokens t
    // WHERE t.user_id = us.id
    // ),
    // '[]'
    // ) AS tokens
    // FROM users us
    // WHERE us.id = :id;
    // """)
    // Mono<UserPopulated> getUserPopulated(UUID id);
}
