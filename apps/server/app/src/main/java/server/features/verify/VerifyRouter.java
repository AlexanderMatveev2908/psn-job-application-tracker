package server.features.verify;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.decorators.flow.api.Api;
import server.decorators.flow.res_api.ResAPI;
import server.features.verify.controllers.GetVerifyCtrl;
import server.features.verify.controllers.PatchVerifyCtrl;
import server.features.verify.controllers.PostVerifyCtrl;
import server.router.RouterAPI;

@RouterAPI("/api/v1/verify") @RequiredArgsConstructor
public class VerifyRouter {
  private final GetVerifyCtrl getCtrl;
  private final PostVerifyCtrl postCtrl;
  private final PatchVerifyCtrl patchCtrl;

  @GetMapping("/confirm-email")
  public Mono<ResponseEntity<ResAPI>> verifyEmail(Api api) {
    return getCtrl.verifyEmail(api);
  }

  @GetMapping("/recover-pwd")
  public Mono<ResponseEntity<ResAPI>> verifyRecoverPwd(Api api) {
    return getCtrl.verifyRecoverPwd(api);
  }

  @GetMapping("/new-email")
  public Mono<ResponseEntity<ResAPI>> confNewEmail(Api api) {
    return getCtrl.confNewEmail(api);
  }

  @PatchMapping("/new-email-2FA")
  public Mono<ResponseEntity<ResAPI>> confNewEmail2FA(Api api) {
    return patchCtrl.confNewEmail2FA(api);
  }

  @PostMapping("/recover-pwd-2FA")
  public Mono<ResponseEntity<ResAPI>> recoverPwd2FA(Api api) {
    return postCtrl.recoverPwd2FA(api);
  }
}
