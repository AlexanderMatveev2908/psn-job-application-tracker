package server.models.applications.svc;

import java.util.UUID;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import server.models.applications.JobAppl;
import server.models.applications.etc.JobApplStatusT;

public interface JobApplRepo extends ReactiveCrudRepository<JobAppl, UUID> {

    @Query("""
            INSERT INTO applications (user_id, company_name, position_name, status, applied_at)
            VALUES (:userId, :companyName, :positionName, CAST(:status AS application_status_t), :appliedAt)
            RETURNING *
                """)
    public Mono<JobAppl> insert(
            UUID userId,
            String companyName,
            String positionName,
            JobApplStatusT status,
            Long appliedAt);

    @Query("SELECT * FROM applications WHERE user_id = :userId")
    public Flux<JobAppl> findByUserId(UUID userId);
}
