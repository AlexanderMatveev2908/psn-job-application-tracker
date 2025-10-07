package server.features.user.controllers;

import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.decorators.flow.api.Api;
import server.decorators.flow.res_api.ResAPI;
import server.features.user.services.DelAccSvc;
import server.lib.security.cookies.MyCookies;

@Component @RequiredArgsConstructor @SuppressFBWarnings({ "EI2" })
public class DelUserCtrl {

  private final DelAccSvc delAccSvc;
  private final MyCookies ckMng;

  public Mono<ResponseEntity<ResAPI>> delAccount(Api api) {

    ResponseCookie ck = ckMng.delJweCookie();

    return delAccSvc.mng(api).then(new ResAPI(200).msg("account deleted").delCookie(ck).build());
  }
}