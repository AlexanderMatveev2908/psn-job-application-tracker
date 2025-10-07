package server.features.verify.middleware;

import org.springframework.stereotype.Component;
import org.springframework.web.server.WebFilterChain;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.decorators.flow.api.Api;
import server.middleware.base_mdw.BaseMdw;
import server.models.token.etc.TokenT;

@Component @RequiredArgsConstructor
public class VerifyRecoverPwdMdw extends BaseMdw {

  @Override
  public Mono<Void> handle(Api api, WebFilterChain chain) {
    return isTarget(api, chain, "/verify/recover-pwd", () -> {
      return limit(api).then(checkQueryCbcHmac(api, TokenT.RECOVER_PWD).then(chain.filter(api)));
    });
  }
}