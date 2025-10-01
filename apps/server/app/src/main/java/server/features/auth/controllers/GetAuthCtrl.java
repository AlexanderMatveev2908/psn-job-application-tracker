package server.features.auth.controllers;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.decorators.flow.Api;
import server.decorators.flow.ResAPI;
import server.features.auth.services.RefreshSvc;

@Component @RequiredArgsConstructor @SuppressFBWarnings({ "EI2" })
public class GetAuthCtrl {

  private final RefreshSvc refreshSvc;

  public Mono<ResponseEntity<ResAPI>> refresh(Api api) {

    return refreshSvc.refresh(api).flatMap(token -> new ResAPI(200).data(Map.of("token", token)).build());
  }
}
