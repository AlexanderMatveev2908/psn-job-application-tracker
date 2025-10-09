package server.models.applications.svc;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import server.models.applications.JobAppl;

@Service
@Transactional
@RequiredArgsConstructor
@SuppressFBWarnings("EI_EXPOSE_REP2")
public class JobApplSvc {

    private final JobApplRepo repo;

    public Flux<JobAppl> findByUserId(UUID userId) {
        return repo.findByUserId(userId);
    }
}
