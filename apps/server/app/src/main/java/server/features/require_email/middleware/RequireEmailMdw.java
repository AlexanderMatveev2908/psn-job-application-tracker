package server.features.require_email.middleware;

import org.springframework.stereotype.Component;
import org.springframework.web.server.WebFilterChain;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.decorators.flow.Api;
import server.decorators.flow.ErrAPI;
import server.middleware.BaseMdw;
import server.models.user.svc.UserSvc;
import server.paperwork.EmailCheck;

@SuppressFBWarnings({ "EI2" }) @Component @RequiredArgsConstructor
public class RequireEmailMdw extends BaseMdw {

  private final UserSvc userSvc;

  @Override
  public Mono<Void> handle(Api api, WebFilterChain chain) {
    return isTarget(api, chain, "/require-email/confirm-email", () -> {
      return limitAndRef(api).flatMap(body -> {
        EmailCheck form = EmailCheck.fromBody(body);

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
