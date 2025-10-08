package server.features.user.middleware;

import org.springframework.stereotype.Component;
import org.springframework.web.server.WebFilterChain;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.decorators.flow.api.Api;
import server.lib.data_structure.parser.Prs;
import server.middleware.base_mdw.BaseMdw;
import server.paperwork.user_validation.pwd_form.PwdForm;

@Component @RequiredArgsConstructor
public class GetAccessMngAccMdw extends BaseMdw {

  @Override
  public Mono<Void> handle(Api api, WebFilterChain chain) {
    return isTarget(api, chain, "/user/manage-account", () -> {
      return limitWithRefBody(api, 10, 30)
          .flatMap(body -> checkUserLoggedPwdToMatch(api, Prs.fromMapToT(body, PwdForm.class).getPassword())
              .then(chain.filter(api)));
    });
  }
}