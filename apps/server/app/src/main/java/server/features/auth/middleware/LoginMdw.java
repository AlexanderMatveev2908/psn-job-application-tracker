package server.features.auth.middleware;

import org.springframework.stereotype.Component;
import org.springframework.web.server.WebFilterChain;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.decorators.flow.Api;
import server.decorators.flow.ErrAPI;
import server.features.auth.paperwork.LoginForm;
import server.middleware.BaseMdw;
import server.models.user.svc.UserSvc;

@Component @RequiredArgsConstructor
public class LoginMdw extends BaseMdw {

    private final UserSvc userSvc;

    @Override
    public Mono<Void> handle(Api api, WebFilterChain chain) {
        return isTarget(api, chain, "/auth/login", () -> {
            return limitAndRef(api).flatMap(bd -> {
                LoginForm form = LoginForm.fromMap(bd);

                return checkForm(api, form).then(userSvc.findByEmail(form.getEmail())
                        .switchIfEmpty(Mono.error(new ErrAPI("user not found", 404))).flatMap(user -> {
                            api.setUserAttr(user);
                            return checkUserPwd(api, form.getPassword()).then(chain.filter(api));
                        }));
            });
        });
    }
}