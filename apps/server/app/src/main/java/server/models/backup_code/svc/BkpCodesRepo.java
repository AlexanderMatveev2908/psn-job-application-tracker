package server.models.backup_code.svc;

import java.util.UUID;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import server.models.backup_code.BkpCodes;

public interface BkpCodesRepo extends ReactiveCrudRepository<BkpCodes, UUID> {

        @Query("""
                        INSERT INTO backup_codes (user_id, code)
                        VALUES (:userId, :code)
                        RETURNING *
                        """)
        public Mono<BkpCodes> insert(UUID userId, String code);

        @Query("""
                        SELECT * FROM backup_codes WHERE user_id = :userId
                        """)
        public Flux<BkpCodes> findByUserId(UUID userId);
}
