package server.models.backup_code.svc;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import server.models.backup_code.BkpCodes;

@Service
@Transactional
@RequiredArgsConstructor
@SuppressFBWarnings("EI_EXPOSE_REP2")
public class BkpCodesSvc {
    private final BkpCodesRepo repo;

    public Flux<BkpCodes> findByUserId(UUID userId) {
        return repo.findByUserId(userId);
    }

    public Mono<BkpCodes> insert(UUID userID, String code) {

        return repo.insert(userID, code).flatMap(saved -> repo.findById(saved.getId()));
    }
}
