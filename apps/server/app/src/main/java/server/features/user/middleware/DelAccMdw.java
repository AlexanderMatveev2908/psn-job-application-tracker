package server.features.user.middleware;

import org.springframework.stereotype.Component;
import org.springframework.web.server.WebFilterChain;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.decorators.flow.api.Api;
import server.middleware.base_mdw.BaseMdw;
import server.models.token.etc.TokenT;

@Component @RequiredArgsConstructor
public class DelAccMdw extends BaseMdw {

  @Override
  public Mono<Void> handle(Api api, WebFilterChain chain) {
    return isTarget(api, chain, "/user/delete-account", () -> {
      return limit(api).then(checkQueryCbcHmacLogged(api, TokenT.MANAGE_ACC).then(chain.filter(api)));
    });
  }
}