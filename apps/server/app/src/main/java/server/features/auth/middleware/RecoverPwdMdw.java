package server.features.auth.middleware;

import org.springframework.stereotype.Component;
import org.springframework.web.server.WebFilterChain;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.decorators.flow.api.Api;
import server.middleware.base_mdw.BaseMdw;
import server.models.token.etc.TokenT;

@Component @RequiredArgsConstructor
public class RecoverPwdMdw extends BaseMdw {

  @Override
  public Mono<Void> handle(Api api, WebFilterChain chain) {
    return isTarget(api, chain, "/auth/recover-pwd", () -> {
      return limit(api, 5, 15).then(checkBodyCbcHmac(api, TokenT.RECOVER_PWD)
          .then(checkPwdForm(api).flatMap(plainTxt -> checkUserPwdToNotMatch(api, plainTxt)).then(chain.filter(api))));
    });
  }
}