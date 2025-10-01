package server.features.user.middleware;

import org.springframework.stereotype.Component;
import org.springframework.web.server.WebFilterChain;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.decorators.flow.Api;
import server.middleware.BaseMdw;

@Component @RequiredArgsConstructor
public class GetUserInfoMdw extends BaseMdw {

  @Override
  public Mono<Void> handle(Api api, WebFilterChain chain) {
    return isTarget(api, chain, "/user/profile", () -> {

      checkJwtOptional(api);

      return chain.filter(api);
    });
  }
}
