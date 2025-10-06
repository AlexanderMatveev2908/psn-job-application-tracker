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

    @Query("""
            UPDATE users
            SET is_verified = TRUE
            WHERE id = :userId
            RETURNING *
            """)
    Mono<User> verifyUser(UUID userId);

    @Query("""
            UPDATE users
            SET password = :newPwd
            WHERE id = :userId
            """)

    Mono<User> changePwd(UUID userId, String newPwd);

    @Query("""
            UPDATE users
            SET tmp_email = :newEmail
            WHERE id  = :userId
            RETURNING *
                        """)
    Mono<User> setTmpEmail(UUID userId, String newEmail);

    @Query("""
            UPDATE users
            SET totp_secret = :totpSecret
            WHERE id = :userId
            RETURNING *
                """)
    Mono<User> setTotpSecret(String totpSecret, UUID userId);

    @Query("""
            UPDATE users
            SET email = tmp_email,
                tmp_email = NULL
            WHERE id = :userId
            RETURNING *;
                    """)
    Mono<User> toggleEmails(UUID userId);
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
