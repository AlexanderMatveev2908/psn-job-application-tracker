package server.features.test.middleware;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import reactor.core.publisher.Mono;
import server.decorators.flow.Api;

@Component
public class GetLimitedMdw implements WebFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exc, WebFilterChain chain) {
        var api = (Api) exc;

        if (api.getPath().equals("/api/v1/test/limited"))
            System.out.println("👮 mdw /limited");

        return chain.filter(api);
    }
}
