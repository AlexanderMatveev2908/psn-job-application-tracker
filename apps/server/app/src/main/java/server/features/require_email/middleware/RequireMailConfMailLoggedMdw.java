package server.features.require_email.middleware;

import org.springframework.stereotype.Component;
import org.springframework.web.server.WebFilterChain;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.decorators.flow.ErrAPI;
import server.decorators.flow.api.Api;
import server.middleware.BaseMdw;

@Component @RequiredArgsConstructor
public class RequireMailConfMailLoggedMdw extends BaseMdw {

  @Override
  public Mono<Void> handle(Api api, WebFilterChain chain) {
    return isTarget(api, chain, "/require-email/confirm-email-logged", () -> {
      return limitAndRef(api).flatMap(body -> {
        return checkJwtMandatory(api).flatMap(dbUser -> {
          if (!dbUser.getEmail().equals(body.get("email")))
            return Mono.error(new ErrAPI("email sent is different from account one", 409));
          else if (dbUser.isVerified())
            return Mono.error(new ErrAPI("user already verified", 409));
          else
            return chain.filter(api);
        });
      });
    });
  }
}