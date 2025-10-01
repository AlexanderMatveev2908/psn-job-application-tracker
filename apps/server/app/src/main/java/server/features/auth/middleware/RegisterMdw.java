package server.features.auth.middleware;

import org.springframework.stereotype.Component;
import org.springframework.web.server.WebFilterChain;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.decorators.flow.Api;
import server.features.auth.paperwork.RegisterForm;
import server.middleware.BaseMdw;
import server.middleware.form_checkers.FormChecker;

@Component
@RequiredArgsConstructor
public class RegisterMdw extends BaseMdw {
    private final FormChecker fCk;

    @Override
    public Mono<Void> handle(Api api, WebFilterChain chain) {
        return isTarget(api, chain, "/auth/register",
                () -> {
                    return limitAndRef(api).flatMap(bd -> {
                        RegisterForm form = RegisterForm.fromMap(bd);

                        return fCk.checkBdForm(api, form)
                                .then(chain.filter(api));
                    });
                });
    }
}
