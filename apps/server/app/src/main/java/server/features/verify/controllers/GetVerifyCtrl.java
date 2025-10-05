package server.features.verify.controllers;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.decorators.flow.Api;
import server.decorators.flow.ResAPI;
import server.features.verify.services.VerifyMailSvc;

@SuppressFBWarnings({ "EI2" }) @Component @RequiredArgsConstructor
public class GetVerifyCtrl {

  private final VerifyMailSvc verifyMailSvc;

  public Mono<ResponseEntity<ResAPI>> verifyEmail(Api api) {
    return verifyMailSvc.mng(api).flatMap(tpl -> new ResAPI(200).msg("user verified").cookie(tpl.getT1())
        .data(Map.of("accessToken", tpl.getT2())).build());
  }

  public Mono<ResponseEntity<ResAPI>> verifyRecoverPwd(Api api) {
    return new ResAPI(200).msg("").data(Map.of("user", api.getUser())).build();
  }
}
