package server.features.user.middleware;

import org.springframework.stereotype.Component;
import org.springframework.web.server.WebFilterChain;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.decorators.flow.ErrAPI;
import server.decorators.flow.api.Api;
import server.middleware.base_mdw.BaseMdw;
import server.models.token.etc.TokenT;

@Component @RequiredArgsConstructor
public class Setup2FAMdw extends BaseMdw {

  @Override
  public Mono<Void> handle(Api api, WebFilterChain chain) {
    return isTarget(api, chain, "/user/2FA", () -> {
      return checkBodyCbcHmacLogged(api, TokenT.MANAGE_ACC).flatMap(user -> {
        if (!user.isVerified())
          return Mono.error(new ErrAPI("user not verified", 401));
        else if (user.getTotpSecret() != null)
          return Mono.error(new ErrAPI("user already has 2FA setup", 409));

        return chain.filter(api);
      });
    });
  }
}