package server.features.user.middleware;

import org.springframework.stereotype.Component;
import org.springframework.web.server.WebFilterChain;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.decorators.flow.api.Api;
import server.middleware.base_mdw.BaseMdw;

@Component @RequiredArgsConstructor
public class GetAccessMngAccMdw extends BaseMdw {

  @Override
  public Mono<Void> handle(Api api, WebFilterChain chain) {
    return isTarget(api, chain, "/user/manage-account", () -> {
      return limit(api, 10, 15).then(checkPwdForm(api))
          .flatMap(pwd -> checkUserLoggedPwdToMatch(api, pwd).then(chain.filter(api)));
    });
  }
}