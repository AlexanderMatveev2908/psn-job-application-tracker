package server.features.require_email;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.decorators.flow.Api;
import server.decorators.flow.ResAPI;
import server.features.require_email.controllers.GetRequireEmail;
import server.features.require_email.controllers.PostRequireEmail;
import server.router.RouterAPI;

@SuppressFBWarnings({ "EI2" }) @RouterAPI("/api/v1/require-email") @RequiredArgsConstructor
public class RequireEmailRouter {
  private final GetRequireEmail getCtrl;
  private final PostRequireEmail postCtrl;

  @GetMapping("/")
  public Mono<ResponseEntity<ResAPI>> getExample(Api api) {
    return getCtrl.example(api);
  }

  @PostMapping("/confirm-email")
  public Mono<ResponseEntity<ResAPI>> confirmEmail(Api api) {
    return postCtrl.confirmEmail(api);
  }
}
