package server.features.require_email.middleware;

import org.springframework.stereotype.Component;
import org.springframework.web.server.WebFilterChain;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.decorators.flow.Api;
import server.decorators.flow.ErrAPI;
import server.middleware.BaseMdw;
import server.models.user.svc.UserSvc;
import server.paperwork.EmailCheck;

@Component @RequiredArgsConstructor
public class RequireMailRecoverPwdMdw extends BaseMdw {

  private final UserSvc userSvc;

  @Override
  public Mono<Void> handle(Api api, WebFilterChain chain) {
    return isTarget(api, chain, "/require-email/recover-pwd", () -> {
      return limitAndRef(api).flatMap(body -> {
        var form = EmailCheck.fromBody(body);

        return checkForm(api, form).then(userSvc.findByEmail(form.getEmail())
            .switchIfEmpty(Mono.error(new ErrAPI("user not found", 404))).flatMap(dbUser -> {
              api.setUserAttr(dbUser);
              return chain.filter(api);
            }));
      });
    });
  }
}