package server.models.token.svc;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import server.models.token.MyToken;
import server.models.token.etc.AlgT;
import server.models.token.etc.TokenT;

@Service
@Transactional
@RequiredArgsConstructor
@SuppressFBWarnings("EI_EXPOSE_REP2")
public class TokenSvc {

    private final TokenRepo repo;

    public Mono<MyToken> createToken(UUID userId,
            TokenT tokenType,
            AlgT algType,
            String hashed,
            long exp) {
        return repo
                .insert(
                        userId,
                        tokenType,
                        algType,
                        hashed,
                        exp)
                .flatMap(saved -> repo.findById(saved.getId()));
    }

    public Flux<MyToken> findByUserId(UUID userId) {
        return repo.findByUserId(userId);
    }
}
