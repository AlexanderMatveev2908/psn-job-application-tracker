package server.features.auth.middleware;

import org.springframework.stereotype.Component;
import org.springframework.web.server.WebFilterChain;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.decorators.flow.Api;
import server.middleware.BaseMdw;
import server.models.token.etc.TokenT;

@Component @RequiredArgsConstructor
public class Login2FAMdw extends BaseMdw {

  @Override
  public Mono<Void> handle(Api api, WebFilterChain chain) {
    return isTarget(api, chain, "/auth/login-2FA", () -> {
      return limit(api).then(check2FA(api, TokenT.LOGIN_2FA)).then(chain.filter(api));
    });
  }
}