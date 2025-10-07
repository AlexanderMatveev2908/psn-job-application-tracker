package server.features.auth.middleware;

import org.springframework.stereotype.Component;
import org.springframework.web.server.WebFilterChain;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.decorators.flow.api.Api;
import server.middleware.BaseMdw;

@Component @RequiredArgsConstructor
public class LogoutMdw extends BaseMdw {

  @Override
  public Mono<Void> handle(Api api, WebFilterChain chain) {
    return isTarget(api, chain, "/auth/logout", () -> {
      return checkJwtOptional(api).then(chain.filter(api));
    });
  }
}