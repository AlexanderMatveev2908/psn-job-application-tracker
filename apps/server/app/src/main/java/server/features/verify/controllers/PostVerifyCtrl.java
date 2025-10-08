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
import server.features.verify.services.VerifyRecoverPwd2FASvc;

@Component @RequiredArgsConstructor @SuppressFBWarnings({ "EI2" })
public class PostVerifyCtrl {

  private final VerifyRecoverPwd2FASvc recoverPwdSvc;

  public Mono<ResponseEntity<ResAPI>> recoverPwd2FA(Api api) {
    return recoverPwdSvc.mng(api).flatMap(clientToken -> {

      Map<String, Object> body = new HashMap<>();
      body.put("cbcHmacToken", clientToken);
      api.putCodesLeftIfPresent(body);

      return new ResAPI(200).data(body).msg("verification successful").build();
    });
  }
}