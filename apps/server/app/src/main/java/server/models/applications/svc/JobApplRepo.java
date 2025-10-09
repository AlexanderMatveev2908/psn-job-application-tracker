package server.models.applications.svc;

import java.util.UUID;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import server.models.applications.JobAppl;

// @formatter:off
public interface JobApplRepo extends ReactiveCrudRepository<JobAppl, UUID> {

        @Query("""
        INSERT INTO applications (user_id, position_name, company_name, status,
        applied_at, notes)
        VALUES (:#{#job.userId}, :#{#job.positionName}, :#{#job.companyName},
        CAST(:#{#job.status} AS application_status_t), :#{#job.appliedAt},
        :#{#job.notes})
        RETURNING *
        """)
        public Mono<JobAppl> insert(JobAppl job);

        @Query("""
        UPDATE applications
        SET company_name = :#{#job.companyName},
                position_name = :#{#job.positionName},
                status = CAST(:#{#job.status} AS application_status_t),
                applied_at = :#{#job.appliedAt},
                notes = :#{#job.notes}
        WHERE id = :#{#job.id} 
        RETURNING *
        """)
        public Mono<JobAppl> put(JobAppl job);

        @Query("SELECT * FROM applications WHERE user_id = :userId")
        public Flux<JobAppl> findByUserId(UUID userId);

        @Query("""
        DELETE FROM applications
        WHERE user_id = :userId
        RETURNING id
        """)
        Flux<String> delByUserId(UUID userid);
        }
