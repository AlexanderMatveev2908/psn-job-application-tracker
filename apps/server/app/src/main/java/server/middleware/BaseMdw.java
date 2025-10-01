package server.middleware;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import com.fasterxml.jackson.core.type.TypeReference;

import reactor.core.publisher.Mono;
import server.decorators.flow.Api;
import server.decorators.flow.ErrAPI;
import server.lib.security.mng_tokens.MyTkMng;
import server.lib.security.mng_tokens.tokens.jwt.etc.MyJwtPayload;
import server.middleware.security.RateLimit;

public abstract class BaseMdw implements WebFilter {
    @Autowired
    private RateLimit rl;
    @Autowired
    private MyTkMng tkMng;

    protected abstract Mono<Void> handle(Api api, WebFilterChain chain);

    @Override
    public Mono<Void> filter(ServerWebExchange exc, WebFilterChain chain) {
        var api = (Api) exc;
        return handle(api, chain);
    }

    private void checkJwt(Api api, boolean throwIfMiss) {
        String token = api.getJwt(throwIfMiss);

        if (token.isBlank() && !throwIfMiss)
            return;

        MyJwtPayload payload = tkMng.checkJwt(token);

        api.setAttr("jwtPayload", payload);
    }

    protected void checkJwtMandatory(Api api) {
        checkJwt(api, true);
    }

    protected void checkJwtOptional(Api api) {
        checkJwt(api, false);
    }

    protected Mono<Map<String, Object>> grabBody(Api api) {
        return api.getBd(new TypeReference<Map<String, Object>>() {
        }).switchIfEmpty(Mono.error(new ErrAPI("data not provided", 400)));
    }

    protected Mono<Map<String, Object>> limitAndRef(Api api, int limit, int minutes) {
        return rl.limit(api, limit, minutes).then(grabBody(api));
    }

    protected Mono<Map<String, Object>> limitAndRef(Api api) {
        return limitAndRef(api, 5, 15);
    }

    protected Mono<Void> isTarget(Api api, WebFilterChain chain, String p, Supplier<Mono<Void>> cb) {
        return !api.isSamePath("/api/v1" + p) ? chain.filter(api) : cb.get();
    }

    // ! logically wrong, server is not suppose to parse some client data
    // ! before actually checking his redis score
    // ! this middleware first grab and parse some data for a request
    // ! that eventually may be rejected
    protected Mono<Void> isTarget(Api api, WebFilterChain chain, String p,
            Function<Map<String, Object>, Mono<Void>> cb) {
        return !api.isSamePath("/api/v1" + p) ? chain.filter(api) : grabBody(api).flatMap(bd -> cb.apply(bd));
    }
}
