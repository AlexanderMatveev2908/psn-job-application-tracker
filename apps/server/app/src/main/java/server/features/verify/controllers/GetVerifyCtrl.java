package server.features.verify.controllers;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.decorators.flow.api.Api;
import server.decorators.flow.res_api.ResAPI;
import server.features.verify.services.VerifyMailSvc;
import server.features.verify.services.VerifyNewEmailSvc;

@SuppressFBWarnings({ "EI2" }) @Component @RequiredArgsConstructor
public class GetVerifyCtrl {

  private final VerifyMailSvc verifyMailSvc;
  private final VerifyNewEmailSvc verifyNewMailSvc;

  public Mono<ResponseEntity<ResAPI>> verifyEmail(Api api) {
    return verifyMailSvc.mng(api).flatMap(tpl -> new ResAPI(200).msg("user verified").cookie(tpl.getT1())
        .data(Map.of("accessToken", tpl.getT2())).build());
  }

  public Mono<ResponseEntity<ResAPI>> verifyRecoverPwd(Api api) {
    var user = api.getUser();
    return new ResAPI(200).msg("token verified").data(Map.of("strategy2FA", user.use2FA())).build();
  }

  public Mono<ResponseEntity<ResAPI>> confNewEmail(Api api) {

    var user = api.getUser();

    return !user.use2FA()
        ? verifyNewMailSvc.simpleFlow(api)
            .flatMap(tpl -> new ResAPI(200).msg("email changed").data(Map.of("accessToken", tpl.getT2()))
                .cookie(tpl.getT1()).build())
        : verifyNewMailSvc.firstSetp2FA(api).flatMap(
            clientToken -> new ResAPI(200).msg("first step passed").data(Map.of("cbcHmacToken", clientToken)).build());
  }
}
