package server.features.user.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.decorators.flow.Api;
import server.decorators.flow.ResAPI;
import server.features.user.services.ChangeMailSvc;
import server.features.user.services.ChangePwdSvc;
import server.features.user.services.Setup2FASvc;

@Component @RequiredArgsConstructor @SuppressFBWarnings({ "EI2" })
public class PatchUserCtrl {

  private final ChangePwdSvc changePwdSvc;
  private final ChangeMailSvc changeMailSvc;
  private final Setup2FASvc setup2FASVC;

  public Mono<ResponseEntity<ResAPI>> changePwd(Api api) {
    return changePwdSvc.mng(api).then(new ResAPI(200).msg("password changed").build());
  }

  public Mono<ResponseEntity<ResAPI>> changeEmail(Api api) {
    return changeMailSvc.mng(api).then(new ResAPI(200).msg("email sent").build());
  }

  public Mono<ResponseEntity<ResAPI>> setup2FA(Api api) {
    return setup2FASVC.mng(api).flatMap(rec -> new ResAPI(200).msg("setup 2FA").data(rec.forClient()).build());
  }

}