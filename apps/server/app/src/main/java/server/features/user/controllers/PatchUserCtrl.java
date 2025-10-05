package server.features.user.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.decorators.flow.Api;
import server.decorators.flow.ResAPI;
import server.features.user.services.ChangePwdSvc;

@Component @RequiredArgsConstructor @SuppressFBWarnings({ "EI2" })
public class PatchUserCtrl {

  private final ChangePwdSvc changePwdSvc;

  public Mono<ResponseEntity<ResAPI>> changePwd(Api api) {

    return changePwdSvc.mng(api).then(new ResAPI(200).msg("password changed").build());

  }
}