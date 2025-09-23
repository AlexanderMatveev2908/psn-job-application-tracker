package server.middleware;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import reactor.core.publisher.Mono;
import server.decorators.flow.Api;

@Component
@Order(0)
public class Ninja implements WebFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exc, WebFilterChain chain) {
        return chain.filter(new Api(exc));
    }
}
