package server.features.user.middleware;

import org.springframework.stereotype.Component;
import org.springframework.web.server.WebFilterChain;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.decorators.flow.api.Api;
import server.middleware.base_mdw.BaseMdw;
import server.models.token.etc.TokenT;

@Component @RequiredArgsConstructor
public class ManageAcc2FAMdw extends BaseMdw {

  @Override
  public Mono<Void> handle(Api api, WebFilterChain chain) {
    return isTarget(api, chain, "/user/manage-account-2FA", () -> {
      return limit(api).then(checkLogged2FA(api, TokenT.MANAGE_ACC_2FA)).then(chain.filter(api));
    });
  }
}