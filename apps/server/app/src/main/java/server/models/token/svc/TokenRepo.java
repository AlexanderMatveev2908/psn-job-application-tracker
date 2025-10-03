package server.models.token.svc;

import java.util.UUID;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import server.models.token.MyToken;
import server.models.token.etc.TokenT;

public interface TokenRepo extends ReactiveCrudRepository<MyToken, UUID> {

  @Query("SELECT * FROM tokens WHERE user_id = :userId")
  Flux<MyToken> findByUserId(UUID userId);

  @Query("""
        SELECT * FROM tokens
        WHERE user_id = :userId
        AND token_type = CAST(:tokenT as token_t)
        LIMIT 1
      """)
  Mono<MyToken> findByUserIdAndTokenT(UUID userId, TokenT tokenT);

  @Query("""
        SELECT * FROM tokens
        WHERE user_id = :userId
        AND token_type = CAST(:tokenT AS token_t)
        AND HASHED = :hashed
        LIMIT 1
      """)
  Mono<MyToken> findByUserIdTypeHash(UUID userId, TokenT tokenT, String hashed);

  @Query("""
      INSERT INTO tokens (user_id, token_type, alg_type, hashed, exp)
      VALUES (:#{#token.userId}, CAST(:#{#token.tokenType} AS token_t), CAST(:#{#token.algType} AS alg_t), :#{#token.hashed}, :#{#token.exp})
      RETURNING *
              """)
  Mono<MyToken> insert(MyToken token);

  @Query("""
      INSERT INTO tokens (id, user_id, token_type, alg_type, hashed, exp)
      VALUES (:#{#token.id}, :#{#token.userId}, CAST(:#{#token.tokenType} AS token_t), CAST(:#{#token.algType} AS alg_t), :#{#token.hashed}, :#{#token.exp})
      RETURNING *
              """)
  Mono<MyToken> insertWithId(MyToken token);

  @Query("""
      DELETE FROM tokens
      WHERE user_id = :userId
      AND token_type = CAST(:tokenT AS token_t)
      RETURNING id
      """)
  Flux<MyToken> deleteByUserIdAndTokenT(UUID userId, TokenT tokenT);

  @Query("""
      DELETE FROM tokens
      WHERE id = :id
      RETURNING id
      """)
  Mono<String> delById(UUID id);
}
