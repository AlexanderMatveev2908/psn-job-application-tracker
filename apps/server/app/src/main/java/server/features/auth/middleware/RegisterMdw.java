package server.features.auth.middleware;

import org.springframework.stereotype.Component;
import org.springframework.web.server.WebFilterChain;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.decorators.flow.Api;
import server.middleware.BaseMdw;
import server.middleware.security.RateLimit;

@Component
@RequiredArgsConstructor
public class RegisterMdw extends BaseMdw {
    private final RateLimit rl;

    @Override
    public Mono<Void> handle(Api api, WebFilterChain chain) {

        if (!api.isSamePath("/api/v1/auth/register"))
            return chain.filter(api);

        return rl.limit(api).then(chain.filter(api));
    }
}