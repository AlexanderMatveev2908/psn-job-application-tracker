package server.models.applications.svc;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import server.models.applications.JobAppl;
import server.models.applications.etc.JobApplStatusT;

@Service
@Transactional
@RequiredArgsConstructor
@SuppressFBWarnings("EI_EXPOSE_REP2")
public class JobApplSvc {

    private final JobApplRepo repo;

    public Mono<JobAppl> insert(
            UUID userId,
            String companyName,
            String positionName,
            JobApplStatusT status,
            Long appliedAt) {

        return repo.insert(userId, companyName, positionName, status, appliedAt)
                .flatMap(saved -> repo.findById(saved.getId()));
    }

    public Flux<JobAppl> findByUserId(UUID userId) {
        return repo.findByUserId(userId);
    }
}
