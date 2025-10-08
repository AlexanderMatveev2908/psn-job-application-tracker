package server.features.auth.middleware;

import org.springframework.stereotype.Component;
import org.springframework.web.server.WebFilterChain;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.decorators.flow.api.Api;
import server.middleware.base_mdw.BaseMdw;
import server.models.token.etc.TokenT;

@Component @RequiredArgsConstructor
public class RecoverPwd2FAMdw extends BaseMdw {

  @Override
  public Mono<Void> handle(Api api, WebFilterChain chain) {
    return isTarget(api, chain, "/auth/recover-pwd-2FA", () -> {
      return limit(api, 5, 15).then(checkBodyCbcHmac(api, TokenT.RECOVER_PWD_2FA)).then(checkPwdReg(api))
          .flatMap(plainTxt -> checkUserPwdToNotMatch(api, plainTxt)).then(chain.filter(api));
    });
  }
}