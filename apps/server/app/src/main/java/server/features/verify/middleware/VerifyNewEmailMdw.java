package server.features.verify.middleware;

import org.springframework.stereotype.Component;
import org.springframework.web.server.WebFilterChain;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.decorators.flow.ErrAPI;
import server.decorators.flow.api.Api;
import server.middleware.BaseMdw;
import server.models.token.etc.TokenT;
import server.models.user.svc.UserSvc;

@Component @RequiredArgsConstructor
public class VerifyNewEmailMdw extends BaseMdw {

  private final UserSvc userSvc;

  @Override
  public Mono<Void> handle(Api api, WebFilterChain chain) {
    return isTarget(api, chain, "/verify/new-email", () -> {
      return limit(api).then(checkQueryCbcHmac(api, TokenT.CHANGE_EMAIL).flatMap(user -> {
        return userSvc.findByEmail(user.getTmpEmail())
            .flatMap(existing -> Mono.<Void>error(new ErrAPI("an account with this email already exists", 409)))
            .switchIfEmpty(chain.filter(api));
      }));
    });
  }
}
