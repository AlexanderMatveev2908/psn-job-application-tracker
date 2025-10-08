package server.features.auth.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.decorators.flow.api.Api;
import server.decorators.flow.res_api.ResAPI;
import server.features.auth.services.RecoverPwdSvc;

@Component @RequiredArgsConstructor @SuppressFBWarnings({ "EI2" })
public class PatchAuthCtrl {

  private final RecoverPwdSvc recoverPwdSvc;

  public Mono<ResponseEntity<ResAPI>> recoverPwd(Api api) {

    return recoverPwdSvc.simpleFlow(api).flatMap(tpl -> new ResAPI(200).msg("password changed").cookie(tpl.getT1())
        .data(Map.of("accessToken", tpl.getT2())).build());
  }

  public Mono<ResponseEntity<ResAPI>> recoverPwd2FA(Api api) {
    return recoverPwdSvc.flow2FA(api).flatMap(tpl -> {

      Map<String, Object> body = new HashMap<>();
      body.put("accessToken", tpl.getT2());
      api.putCodesLeftIfPresent(body);

      return new ResAPI(200).msg("password changed").data(body).cookie(tpl.getT1()).build();
    });
  }
}