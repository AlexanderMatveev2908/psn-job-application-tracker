package server.features.require_email;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.decorators.flow.api.Api;
import server.decorators.flow.res_api.ResAPI;
import server.features.require_email.controllers.PostRequireEmail;
import server.router.RouterAPI;

@SuppressFBWarnings({ "EI2" }) @RouterAPI("/api/v1/require-email") @RequiredArgsConstructor
public class RequireEmailRouter {
  private final PostRequireEmail postCtrl;

  @PostMapping("/recover-pwd")
  public Mono<ResponseEntity<ResAPI>> getExample(Api api) {
    return postCtrl.recoverPwd(api);
  }

  @PostMapping({ "/confirm-email", "/confirm-email-logged" })
  public Mono<ResponseEntity<ResAPI>> requireMailConfMail(Api api) {
    return postCtrl.requireMailConfMail(api);
  }
}
