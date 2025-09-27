package server.features.auth.middleware.register;

import org.springframework.stereotype.Component;
import org.springframework.web.server.WebFilterChain;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.decorators.flow.Api;
import server.features.auth.paperwork.RegisterForm;
import server.middleware.CmnMdw;
import server.middleware.BaseMdw;
import server.middleware.form_checkers.FormChecker;

@Component
@RequiredArgsConstructor
public class RegisterMdw extends BaseMdw {
    private final FormChecker fCk;
    private final CmnMdw cmnMdw;

    @Override
    public Mono<Void> handle(Api api, WebFilterChain chain) {

        return cmnMdw.isTarget(api, "/auth/register").flatMap(m -> {
            return !m ? chain.filter(api) : cmnMdw.limitAndRef(api).flatMap(bd -> {
                RegisterForm form = RegisterForm.mapToForm(bd);

                return fCk.checkBdForm(api, form)
                        .then(chain.filter(api));

            });
        });
    }
}