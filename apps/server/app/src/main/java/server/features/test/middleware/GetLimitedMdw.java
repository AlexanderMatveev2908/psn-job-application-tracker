package server.features.test.middleware;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import reactor.core.publisher.Mono;
import server.decorators.flow.Api;
import server.middleware.security.RateLimit;

@Component
public class GetLimitedMdw implements WebFilter {

    private final RateLimit rl;

    public GetLimitedMdw(RateLimit rl) {
        this.rl = rl;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exc, WebFilterChain chain) {
        var api = (Api) exc;

        if (!api.getPath().equals("/api/v1/test/limited"))
            return chain.filter(api);

        return rl.limit(api, 5, 15)
                .then(Mono.defer(() -> chain.filter(api)));

    }
}
