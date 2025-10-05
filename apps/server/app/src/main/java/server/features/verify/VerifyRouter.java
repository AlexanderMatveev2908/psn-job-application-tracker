package server.features.verify;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.decorators.flow.Api;
import server.decorators.flow.ResAPI;
import server.features.verify.controllers.GetVerifyCtrl;
import server.router.RouterAPI;

@RouterAPI("/api/v1/verify") @RequiredArgsConstructor
public class VerifyRouter {
  private final GetVerifyCtrl getCtrl;

  @GetMapping("/confirm-email")
  public Mono<ResponseEntity<ResAPI>> verifyEmail(Api api) {
    return getCtrl.verifyEmail(api);
  }

  @GetMapping("/recover-pwd")
  public Mono<ResponseEntity<ResAPI>> verifyRecoverPwd(Api api) {
    return getCtrl.verifyRecoverPwd(api);
  }
}
