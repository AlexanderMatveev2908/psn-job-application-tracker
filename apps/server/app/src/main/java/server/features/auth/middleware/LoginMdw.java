package server.features.auth.middleware;

import org.springframework.stereotype.Component;
import org.springframework.web.server.WebFilterChain;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.decorators.flow.Api;
import server.features.auth.paperwork.LoginForm;
import server.middleware.BaseMdw;

@Component @RequiredArgsConstructor
public class LoginMdw extends BaseMdw {

    @Override
    public Mono<Void> handle(Api api, WebFilterChain chain) {
        return isTarget(api, chain, "/auth/login", () -> {
            return limitAndRef(api).flatMap(bd -> {
                LoginForm form = LoginForm.fromMap(bd);

                return checkForm(api, form).then(chain.filter(api));
            });
        });
    }
}