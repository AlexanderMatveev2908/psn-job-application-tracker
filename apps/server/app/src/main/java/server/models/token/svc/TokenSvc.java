package server.models.token.svc;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import server.models.token.MyToken;
import server.models.token.etc.TokenT;

@Service @Transactional @RequiredArgsConstructor @SuppressFBWarnings("EI_EXPOSE_REP2")
public class TokenSvc {

    private final TokenRepo repo;

    public Mono<MyToken> insert(MyToken arg) {
        return repo.insert(arg);
    }

    public Flux<MyToken> findByUserId(UUID userId) {
        return repo.findByUserId(userId);
    }

    public Mono<Integer> deleteByUserIdAndTokenT(UUID userId, TokenT tokenT) {
        return repo.deleteByUserIdAndTokenT(userId, tokenT).collectList().flatMap(ids -> {
            System.out.println("🧹 tokens deleted => " + ids.size());
            return Mono.just(ids.size());
        });
    }
}
