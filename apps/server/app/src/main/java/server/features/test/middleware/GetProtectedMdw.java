package server.features.test.middleware;

import org.springframework.stereotype.Component;
import org.springframework.web.server.WebFilterChain;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.decorators.flow.api.Api;
import server.middleware.base_mdw.BaseMdw;

@Component @RequiredArgsConstructor
public class GetProtectedMdw extends BaseMdw {

  @Override
  public Mono<Void> handle(Api api, WebFilterChain chain) {
    return isTarget(api, chain, "/test/protected", () -> {
      return checkJwtMandatory(api).then(chain.filter(api));
    });
  }
}