package server.features.verify.middleware;

import org.springframework.stereotype.Component;
import org.springframework.web.server.WebFilterChain;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.decorators.flow.api.Api;
import server.middleware.base_mdw.BaseMdw;
import server.models.token.etc.TokenT;

@Component @RequiredArgsConstructor
public class ChangeEmail2FAMdw extends BaseMdw {

  @Override
  public Mono<Void> handle(Api api, WebFilterChain chain) {
    return isTarget(api, chain, "/verify/new-email-2FA", () -> {
      return limit(api, 5, 15).then(check2FA(api, TokenT.CHANGE_EMAIL_2FA)).then(chain.filter(api));
    });
  }
}