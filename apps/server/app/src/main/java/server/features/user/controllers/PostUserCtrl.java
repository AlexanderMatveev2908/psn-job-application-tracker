package server.features.user.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.decorators.flow.api.Api;
import server.decorators.flow.res_api.ResAPI;
import server.features.user.services.AccessManageAccSvc;

@Component @RequiredArgsConstructor @SuppressFBWarnings({ "EI2" })
public class PostUserCtrl {

  private final AccessManageAccSvc manageAccSvc;

  public Mono<ResponseEntity<ResAPI>> getAccessMngAcc(Api api) {

    return manageAccSvc.simpleAccess(api)
        .flatMap(clientToken -> new ResAPI(200).msg("ok").data(Map.of("cbcHmacToken", clientToken)).build());
  }

  public Mono<ResponseEntity<ResAPI>> manageAcc2FA(Api api) {
    return manageAccSvc.access2FA(api).flatMap(clientToken -> {

      Map<String, Object> data = new HashMap<>();
      data.put("cbcHmacToken", clientToken);
      api.putCodesLeftIfPresent(data);

      return new ResAPI(200).msg("access grated").data(data).build();
    });
  }
}