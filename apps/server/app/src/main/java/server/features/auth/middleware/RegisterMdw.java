package server.features.auth.middleware;

import org.springframework.stereotype.Component;
import org.springframework.web.server.WebFilterChain;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.decorators.flow.api.Api;
import server.features.auth.paperwork.RegisterForm;
import server.middleware.base_mdw.BaseMdw;

@Component @RequiredArgsConstructor
public class RegisterMdw extends BaseMdw {

    @Override
    public Mono<Void> handle(Api api, WebFilterChain chain) {
        return isTarget(api, chain, "/auth/register", () -> {
            return limit(api, 5, 15).then(checkBodyForm(api, RegisterForm.class)).then(chain.filter(api));
        });
    }
}
