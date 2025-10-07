package server.features.verify.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.decorators.flow.api.Api;
import server.decorators.flow.res_api.ResAPI;
import server.features.verify.services.VerifyNewEmailSvc;

@Component @RequiredArgsConstructor @SuppressFBWarnings({ "EI2" })
public class PatchVerifyCtrl {
  private final VerifyNewEmailSvc verifyNewMailSvc;

  public Mono<ResponseEntity<ResAPI>> confNewEmail2FA(Api api) {
    return verifyNewMailSvc.finalStep2FA(api).flatMap(tpl -> {

      Map<String, Object> body = new HashMap<>();
      body.put("accessToken", tpl.getT2());
      api.putCodesLeftIfPresent(body);

      return new ResAPI(200).msg("email changed").data(body).cookie(tpl.getT1()).build();
    });
  }
}