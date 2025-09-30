package server.models.token.svc;

import java.util.UUID;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import server.models.token.MyToken;

public interface TokenRepo extends ReactiveCrudRepository<MyToken, UUID> {

        @Query("SELECT * FROM tokens WHERE user_id = :userId")
        Flux<MyToken> findByUserId(UUID userId);

        @Query("""
                            INSERT INTO tokens (user_id, token_type, alg_type, hashed, exp)
                            VALUES (:#{#token.userId}, CAST(:#{#token.tokenType} AS token_t), CAST(:#{#token.algType} AS alg_t), :#{#token.hashed}, :#{#token.exp})
                            RETURNING *
                        """)
        Mono<MyToken> insert(MyToken token);

}
