package server.middleware;

import java.util.Map;
import java.util.UUID;
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
import server.lib.security.hash.MyHashMng;
import server.lib.security.mng_tokens.TkMng;
import server.lib.security.mng_tokens.etc.MyTkPayload;
import server.middleware.security.RateLimit;
import server.models.token.etc.TokenT;
import server.models.token.svc.TokenSvc;
import server.models.user.User;
import server.models.user.svc.UserSvc;

public abstract class BaseMdw implements WebFilter {

    @Autowired
    private RateLimit rl;
    @Autowired
    private TkMng tkMng;
    @Autowired
    private UserSvc userSvc;
    @Autowired
    private TokenSvc tokenSvc;
    @Autowired
    private MyHashMng hashMng;

    protected abstract Mono<Void> handle(Api api, WebFilterChain chain);

    @Override
    public Mono<Void> filter(ServerWebExchange exc, WebFilterChain chain) {
        var api = (Api) exc;
        return handle(api, chain);
    }

    private Mono<User> checkJwt(Api api, boolean throwIfMiss) {
        String jwt = api.getJwt();
        String jwe = api.getJwe();

        if (jwt.isBlank()) {
            if (!throwIfMiss && jwe.isBlank())
                return Mono.empty();
            else
                return Mono.error(new ErrAPI("jwt_not_provided", 401));
        }

        MyTkPayload payload = tkMng.checkJwt(jwt);

        return userSvc.findById(payload.userId()).switchIfEmpty(Mono.error(new ErrAPI("jwt_invalid", 401)))
                .map(user -> {
                    api.setAttr("user", user);
                    return user;
                });

    }

    protected Mono<User> checkJwtMandatory(Api api) {
        return checkJwt(api, true);
    }

    protected Mono<User> checkJwtOptional(Api api) {
        return checkJwt(api, false);
    }

    protected Mono<User> checkCbcHmac(Api api, TokenT tokenT) {
        String token = api.getCbcHmac();

        if (token.isBlank())
            return Mono.error(new ErrAPI("cbc_hmac_not_provided", 401));

        try {
            MyTkPayload payload = tkMng.checkCbcHmac(token);

            return tokenSvc.findByHash(payload.userId(), tokenT, hashMng.hmacHash(token))
                    .switchIfEmpty(Mono.error(new ErrAPI("cbc_hmac_not_found", 401)))
                    .flatMap(dbToken -> userSvc.findById(dbToken.getUserId())
                            .switchIfEmpty(Mono.error(new ErrAPI("cbc_hmac_invalid", 401))).map(dbUser -> {
                                api.setAttr("user", dbUser);
                                return dbUser;
                            }));
        } catch (Exception err) {
            Map<String, Object> data = null;

            if (err instanceof ErrAPI errInst)
                data = errInst.getData();

            return (data != null && data.get("idCbcHmacRm") instanceof UUID tokenId)
                    ? tokenSvc.delById(tokenId).then(Mono.error(err))
                    : Mono.error(err);
        }

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
