package server.middleware.base_mdw;

import java.util.Map;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import com.fasterxml.jackson.core.type.TypeReference;

import reactor.core.publisher.Mono;
import server.decorators.flow.ErrAPI;
import server.decorators.flow.api.Api;
import server.middleware.base_mdw.etc.BaseLimitMdw;
import server.middleware.base_mdw.etc.BasePwdMdw;
import server.middleware.base_mdw.etc.BaseTokensMdw;
import server.middleware.form_checkers.FormChecker;
import server.middleware.security.Check2FAMdw;
import server.middleware.security.TokenCheckerMdw;
import server.middleware.security.UserPwdCheckerMdw;
import server.middleware.security.RateLimit;
import server.models.token.etc.TokenT;
import server.paperwork.tfa.TFAForm;

public abstract class BaseMdw implements WebFilter, BaseTokensMdw, BasePwdMdw, BaseLimitMdw {

    @Autowired
    private RateLimit rl;
    @Autowired
    private FormChecker formCk;
    @Autowired
    private TokenCheckerMdw tokenCk;
    @Autowired
    private UserPwdCheckerMdw checkUserMdw;
    @Autowired
    private Check2FAMdw tfaCheck;

    protected abstract Mono<Void> handle(Api api, WebFilterChain chain);

    @Override
    public Mono<Void> filter(ServerWebExchange exc, WebFilterChain chain) {
        var api = (Api) exc;
        return handle(api, chain);
    }

    @Override
    public TokenCheckerMdw useTokenChecker() {
        return tokenCk;
    }

    @Override
    public UserPwdCheckerMdw useUserPwdChecker() {
        return checkUserMdw;
    }

    @Override
    public RateLimit useLimit() {
        return rl;
    }

    @Override
    public Mono<Map<String, Object>> grabBody(Api api) {
        return api.getBd(new TypeReference<Map<String, Object>>() {
        }).switchIfEmpty(Mono.error(new ErrAPI("data not provided", 400)));
    }

    @Override
    public <T> Mono<Void> checkForm(Api api, T form) {
        return formCk.checkForm(api, form);
    }

    protected Mono<Void> check2FA(Api api, TokenT tokenT) {
        return checkBodyCbcHmac(api, tokenT).flatMap(user -> grabBody(api).flatMap(body -> {
            var form = TFAForm.fromMap(body);
            return checkForm(api, form).then(tfaCheck.check2FA(api, form));
        }));
    }

    protected Mono<Void> checkLogged2FA(Api api, TokenT tokenT) {
        return checkJwtMandatory(api).then(checkBodyCbcHmac(api, tokenT).flatMap(user -> grabBody(api).flatMap(body -> {
            var form = TFAForm.fromMap(body);
            return checkForm(api, form).then(tfaCheck.check2FA(api, form));
        })));
    }

    protected Mono<Void> checkUserLoggedPwdToMatch(Api api, String plainText) {
        return checkJwtMandatory(api).flatMap(user -> checkUserPwdToMatch(api, plainText));
    }

    protected Mono<Void> isTarget(Api api, WebFilterChain chain, String p, Supplier<Mono<Void>> cb) {
        return !api.isSamePath("/api/v1" + p) ? chain.filter(api) : cb.get();
    }

}
