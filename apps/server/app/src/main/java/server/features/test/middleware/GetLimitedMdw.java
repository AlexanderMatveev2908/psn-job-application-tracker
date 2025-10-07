package server.features.test.middleware;

import org.springframework.stereotype.Component;
import org.springframework.web.server.WebFilterChain;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.decorators.flow.api.Api;
import server.middleware.base_mdw.BaseMdw;
import server.middleware.security.RateLimit;

@Component @RequiredArgsConstructor
public class GetLimitedMdw extends BaseMdw {

    private final RateLimit rl;

    @Override
    public Mono<Void> handle(Api api, WebFilterChain chain) {

        if (!api.isSamePath("/api/v1/test/limited"))
            return chain.filter(api);

        return rl.limit(api).then(chain.filter(api));

    }
}
