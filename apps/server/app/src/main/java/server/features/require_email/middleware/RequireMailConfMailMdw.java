package server.features.require_email.middleware;

import org.springframework.stereotype.Component;
import org.springframework.web.server.WebFilterChain;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.decorators.flow.ErrAPI;
import server.decorators.flow.api.Api;
import server.lib.data_structure.parser.Prs;
import server.middleware.base_mdw.BaseMdw;
import server.models.user.svc.UserSvc;
import server.paperwork.user_validation.email_form.EmailForm;

@SuppressFBWarnings({ "EI2" }) @Component @RequiredArgsConstructor
public class RequireMailConfMailMdw extends BaseMdw {

  private final UserSvc userSvc;

  @Override
  public Mono<Void> handle(Api api, WebFilterChain chain) {
    return isTarget(api, chain, "/require-email/confirm-email", () -> {
      return limitWithRefBody(api).flatMap(body -> {
        EmailForm form = Prs.fromMapToT(body, EmailForm.class);

        return checkForm(api, form).then(userSvc.findByEmail(form.getEmail())
            .switchIfEmpty(Mono.error(new ErrAPI("user not found", 404))).flatMap(dbUser -> {
              if (dbUser.isVerified())
                return Mono.error(new ErrAPI("user already verified", 409));

              api.setUserAttr(dbUser);

              return chain.filter(api);
            }));
      });
    });
  }
}
