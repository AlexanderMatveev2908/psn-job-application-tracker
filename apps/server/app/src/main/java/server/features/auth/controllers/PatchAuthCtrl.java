package server.features.auth.controllers;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.decorators.flow.Api;
import server.decorators.flow.ResAPI;
import server.features.auth.services.RecoverPwdSvc;

@Component @RequiredArgsConstructor @SuppressFBWarnings({ "EI2" })
public class PatchAuthCtrl {

  private final RecoverPwdSvc recoverPwdSvc;

  public Mono<ResponseEntity<ResAPI>> recoverPwd(Api api) {

    return recoverPwdSvc.mng(api).flatMap(tpl -> new ResAPI(200).msg("password changed").cookie(tpl.getT1())
        .data(Map.of("accessToken", tpl.getT2())).build());
  }
}