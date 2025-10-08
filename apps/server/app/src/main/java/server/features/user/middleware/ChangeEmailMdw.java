package server.features.user.middleware;

import org.springframework.stereotype.Component;
import org.springframework.web.server.WebFilterChain;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.decorators.flow.ErrAPI;
import server.decorators.flow.api.Api;
import server.lib.data_structure.parser.Prs;
import server.middleware.base_mdw.BaseMdw;
import server.models.token.etc.TokenT;
import server.models.user.svc.UserSvc;
import server.paperwork.user_validation.email_form.EmailForm;

@Component @RequiredArgsConstructor
public class ChangeEmailMdw extends BaseMdw {

  private final UserSvc userSvc;

  @Override
  public Mono<Void> handle(Api api, WebFilterChain chain) {
    return isTarget(api, chain, "/user/change-email", () -> {
      return limit(api, 3, 15).then(checkBodyCbcHmacLogged(api, TokenT.MANAGE_ACC)).then(grabBody(api).flatMap(body -> {
        EmailForm form = Prs.fromMapToT(body, EmailForm.class);

        return checkForm(api, form).then(Mono.defer(() -> {
          if (api.getUser().getEmail().equals(form.getEmail()))
            return Mono.error(new ErrAPI("new email must be different from old one", 400));

          return userSvc.findByEmail(form.getEmail())
              .flatMap(existing -> Mono.<Void>error(new ErrAPI("an account with this email already exists", 409)))
              .switchIfEmpty(chain.filter(api));
        }));
      }));
    });
  }
}