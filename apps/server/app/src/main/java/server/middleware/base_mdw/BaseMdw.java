package server.middleware.base_mdw;

import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import com.fasterxml.jackson.core.type.TypeReference;

import reactor.core.publisher.Mono;
import server.decorators.flow.ErrAPI;
import server.decorators.flow.api.Api;
import server.lib.data_structure.parser.Prs;
import server.middleware.base_mdw.etc.interfaces.BaseLimitMdw;
import server.middleware.base_mdw.etc.interfaces.BasePwdMdw;
import server.middleware.base_mdw.etc.interfaces.BaseTokensMdw;
import server.middleware.base_mdw.etc.services_mdw.Check2FASvcMdw;
import server.middleware.base_mdw.etc.services_mdw.FormCheckerSvcMdw;
import server.middleware.base_mdw.etc.services_mdw.RateLimitSvcMdw;
import server.middleware.base_mdw.etc.services_mdw.TokenCheckerSvcMdw;
import server.middleware.base_mdw.etc.services_mdw.UserPwdCheckerSvcMdw;
import server.models.token.etc.TokenT;
import server.paperwork.user_validation.pwd_form.PwdForm;
import server.paperwork.user_validation.tfa.TFAForm;

public abstract class BaseMdw implements WebFilter, BaseTokensMdw, BasePwdMdw, BaseLimitMdw {

    @Autowired
    private RateLimitSvcMdw rl;
    @Autowired
    private FormCheckerSvcMdw formCk;
    @Autowired
    private TokenCheckerSvcMdw tokenCk;
    @Autowired
    private UserPwdCheckerSvcMdw checkUserMdw;
    @Autowired
    private Check2FASvcMdw tfaCheck;

    protected abstract Mono<Void> handle(Api api, WebFilterChain chain);

    @Override
    public Mono<Void> filter(ServerWebExchange exc, WebFilterChain chain) {
        var api = (Api) exc;
        return handle(api, chain);
    }

    @Override
    public TokenCheckerSvcMdw useTokenChecker() {
        return tokenCk;
    }

    @Override
    public UserPwdCheckerSvcMdw useUserPwdChecker() {
        return checkUserMdw;
    }

    @Override
    public RateLimitSvcMdw useLimit() {
        return rl;
    }

    @Override
    public Mono<Map<String, Object>> grabBody(Api api) {
        return api.getBd(new TypeReference<Map<String, Object>>() {
        }).switchIfEmpty(Mono.error(new ErrAPI("data not provided", 400)));
    }

    private <T> Mono<Void> checkForm(Api api, T form) {
        return formCk.check(api, form);
    }

    private <T> Mono<T> extractAndCheckForm(Api api, Map<String, Object> arg, Class<T> cls) {
        T form = Prs.fromMapToT(arg, cls);

        return checkForm(api, form).thenReturn(form);
    }

    protected <T> Mono<T> checkBodyForm(Api api, Class<T> cls) {
        return grabBody(api).flatMap(body -> extractAndCheckForm(api, body, cls));
    }

    protected <T> Mono<T> checkMultipartForm(Api api, Class<T> cls) {
        Optional<Map<String, Object>> parsedFormData = api.getParsedForm();

        return Mono.defer(() -> parsedFormData.isPresent() ? Mono.just(parsedFormData.get()) : grabBody(api))
                .flatMap(mapArg -> extractAndCheckForm(api, mapArg, cls));

    }

    protected Mono<String> checkPwdForm(Api api) {
        return checkBodyForm(api, PwdForm.class).map(form -> form.getPassword());
    }

    protected Mono<Void> check2FA(Api api, TokenT tokenT) {
        return checkBodyCbcHmac(api, tokenT).flatMap(user -> grabBody(api).flatMap(body -> {
            var form = TFAForm.fromMap(body);
            return checkForm(api, form).then(tfaCheck.check2FA(api, form));
        }));
    }

    protected Mono<Void> checkLogged2FA(Api api, TokenT tokenT) {
        return checkJwtMandatory(api).then(check2FA(api, tokenT));
    }

    protected Mono<Void> checkUserLoggedPwdToMatch(Api api, String plainText) {
        return checkJwtMandatory(api).flatMap(user -> checkUserPwdToMatch(api, plainText));
    }

    protected Mono<Void> isTarget(Api api, WebFilterChain chain, String path, Supplier<Mono<Void>> cb) {
        return !api.isSamePath("/api/v1" + path) ? chain.filter(api) : cb.get();
    }

    protected Mono<Void> isTarget(Api api, WebFilterChain chain, String path, HttpMethod method,
            Supplier<Mono<Void>> cb) {
        return !api.isSamePath("/api/v1" + path, method) ? chain.filter(api) : cb.get();
    }

    protected Mono<Void> isSubPathOf(Api api, WebFilterChain chain, String p, Supplier<Mono<Void>> cb) {
        return !api.isSubPathOf("/api/v1" + p) ? chain.filter(api) : cb.get();
    }

    protected Mono<Void> isSubPathOf(Api api, WebFilterChain chain, String p, HttpMethod method,
            Supplier<Mono<Void>> cb) {
        return !api.isSubPathOf("/api/v1" + p, method) ? chain.filter(api) : cb.get();
    }

}
