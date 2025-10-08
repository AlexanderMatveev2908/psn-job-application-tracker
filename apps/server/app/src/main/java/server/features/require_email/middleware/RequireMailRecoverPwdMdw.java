package server.features.require_email.middleware;

import org.springframework.stereotype.Component;
import org.springframework.web.server.WebFilterChain;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.decorators.flow.ErrAPI;
import server.decorators.flow.api.Api;
import server.lib.data_structure.parser.Prs;
import server.middleware.base_mdw.BaseMdw;
import server.models.user.svc.UserSvc;
import server.paperwork.user_validation.email_form.EmailForm;

@Component @RequiredArgsConstructor
public class RequireMailRecoverPwdMdw extends BaseMdw {

  private final UserSvc userSvc;

  @Override
  public Mono<Void> handle(Api api, WebFilterChain chain) {
    return isTarget(api, chain, "/require-email/recover-pwd", () -> {
      return limitWithRefBody(api, 5, 15).flatMap(body -> {
        EmailForm form = Prs.fromMapToT(body, EmailForm.class);

        return checkForm(api, form).then(userSvc.findByEmail(form.getEmail())
            .switchIfEmpty(Mono.error(new ErrAPI("user not found", 404))).flatMap(dbUser -> {
              api.setUserAttr(dbUser);
              return chain.filter(api);
            }));
      });
    });
  }
}