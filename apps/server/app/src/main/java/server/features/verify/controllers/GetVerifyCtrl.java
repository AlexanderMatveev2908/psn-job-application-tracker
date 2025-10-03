package server.features.verify.controllers;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Mono;
import server.decorators.flow.Api;
import server.decorators.flow.ResAPI;

@Component
public class GetVerifyCtrl {

  public Mono<ResponseEntity<ResAPI>> verifyEmail(Api api) {
    return new ResAPI(200).msg("check").data(Map.of("user", api.getUser())).build();
  }
}
