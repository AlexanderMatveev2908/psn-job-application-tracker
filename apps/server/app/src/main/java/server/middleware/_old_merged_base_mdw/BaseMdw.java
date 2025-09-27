package server.middleware._old_merged_base_mdw;

import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import reactor.core.publisher.Mono;
import server.decorators.flow.Api;

public abstract class BaseMdw implements WebFilter {
    protected abstract Mono<Void> handle(Api api, WebFilterChain chain);

    @Override
    public Mono<Void> filter(ServerWebExchange exc, WebFilterChain chain) {
        var api = (Api) exc;

        return handle(api, chain);
    }
}
