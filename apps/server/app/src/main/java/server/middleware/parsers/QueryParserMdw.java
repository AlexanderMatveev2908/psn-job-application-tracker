package server.middleware.parsers;

import java.util.Map;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import reactor.core.publisher.Mono;
import server.decorators.flow.api.Api;

@Component @Order(30)
public class QueryParserMdw implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exc, WebFilterChain chain) {
        Api api = (Api) exc;

        String query = api.getQuery();
        Map<String, Object> parsedQuery = ParserManager.nestDict(query);

        api.setParsedQueryAttr(parsedQuery);

        return chain.filter(api);
    }
}
