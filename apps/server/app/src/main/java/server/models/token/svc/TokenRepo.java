package server.models.token.svc;

import java.util.UUID;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import server.models.token.MyToken;
import server.models.token.etc.AlgT;
import server.models.token.etc.TokenT;

public interface TokenRepo extends ReactiveCrudRepository<MyToken, UUID> {

        @Query("SELECT * FROM tokens WHERE user_id = :userId")
        Flux<MyToken> findByUserId(UUID userId);

        @Query("""
                        INSERT INTO tokens (user_id, token_type, alg_type, hashed, exp)
                        VALUES (:userId, CAST(:tokenType AS token_t), CAST(:algType AS alg_t), :hashed, :exp)
                        RETURNING *
                        """)
        Mono<MyToken> insert(
                        UUID userId,
                        TokenT tokenType,
                        AlgT algType,
                        String hashed,
                        long exp);

        // @Query("""
        // INSERT INTO tokens (user_id, token_type, alg_type, hashed, exp)
        // VALUES (:#{#arg.userId}, CAST(:#{#arg.tokenType} AS token_t),
        // CAST(:#{#arg.algType} AS alg_t), :#{#arg.hashed}, :#{#arg.exp})
        // RETURNING *
        // """)
        // Mono<MyToken> insert(MyToken arg);

}
