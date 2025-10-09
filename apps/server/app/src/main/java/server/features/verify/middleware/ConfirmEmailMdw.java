package server.features.verify.middleware;

import org.springframework.stereotype.Component;
import org.springframework.web.server.WebFilterChain;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.decorators.flow.ErrAPI;
import server.decorators.flow.api.Api;
import server.middleware.base_mdw.BaseMdw;
import server.models.token.etc.TokenT;

@Component @RequiredArgsConstructor
public class ConfirmEmailMdw extends BaseMdw {

  @Override
  public Mono<Void> handle(Api api, WebFilterChain chain) {
    return isTarget(api, chain, "/verify/confirm-email", () -> {
      return limit(api, 5, 15).then(checkQueryCbcHmac(api, TokenT.CONF_EMAIL).flatMap(user -> {
        if (user.isVerified())
          return Mono.error(new ErrAPI("user already verified", 409));

        return chain.filter(api);
      }));
    });
  }
}
