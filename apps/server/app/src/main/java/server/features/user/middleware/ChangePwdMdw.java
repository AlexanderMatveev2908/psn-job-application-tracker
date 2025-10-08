package server.features.user.middleware;

import org.springframework.stereotype.Component;
import org.springframework.web.server.WebFilterChain;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.decorators.flow.api.Api;
import server.middleware.base_mdw.BaseMdw;
import server.models.token.etc.TokenT;

@Component @RequiredArgsConstructor
public class ChangePwdMdw extends BaseMdw {

  @Override
  public Mono<Void> handle(Api api, WebFilterChain chain) {
    return isTarget(api, chain, "/user/change-pwd", () -> {
      return limit(api, 5, 15).then(checkBodyCbcHmacLogged(api, TokenT.MANAGE_ACC)
          .then(checkPwdReg(api).flatMap(plainTxt -> checkUserPwdToNotMatch(api, plainTxt).then(chain.filter(api)))));
    });
  }
}