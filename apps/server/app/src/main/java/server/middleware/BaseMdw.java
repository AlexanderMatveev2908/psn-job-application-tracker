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
import server.middleware.form_checkers.FormChecker;
import server.middleware.security.CheckTokenMdw;
import server.middleware.security.RateLimit;
import server.models.token.etc.TokenT;
import server.models.user.User;

public abstract class BaseMdw implements WebFilter {

    @Autowired
    private RateLimit rl;
    @Autowired
    private FormChecker formCk;
    @Autowired
    private CheckTokenMdw tokenCk;

    protected abstract Mono<Void> handle(Api api, WebFilterChain chain);

    @Override
    public Mono<Void> filter(ServerWebExchange exc, WebFilterChain chain) {
        var api = (Api) exc;
        return handle(api, chain);
    }

    protected Mono<User> checkJwtMandatory(Api api) {
        return tokenCk.checkJwt(api, true);
    }

    protected Mono<User> checkJwtOptional(Api api) {
        return tokenCk.checkJwt(api, false);
    }

    protected Mono<User> checkCbcHmac(Api api, TokenT tokenT) {
        return tokenCk.checkCbcHmac(api, tokenT);
    }

    protected <T> Mono<Void> checkForm(Api api, T form) {
        return formCk.checkForm(api, form);
    }

    protected Mono<Map<String, Object>> grabBody(Api api) {
        return api.getBd(new TypeReference<Map<String, Object>>() {
        }).switchIfEmpty(Mono.error(new ErrAPI("data not provided", 400)));
    }

    protected Mono<Void> limit(Api api, int limit, int minutes) {
        return rl.limit(api, limit, minutes);
    }

    protected Mono<Void> limit(Api api) {
        return limit(api, 5, 15);
    }

    protected Mono<Map<String, Object>> limitAndRef(Api api, int limit, int minutes) {
        return limit(api, limit, minutes).then(grabBody(api));
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
