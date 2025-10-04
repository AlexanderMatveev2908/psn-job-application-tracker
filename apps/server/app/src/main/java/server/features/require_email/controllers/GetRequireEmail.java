package server.features.require_email.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.decorators.flow.Api;
import server.decorators.flow.ResAPI;

@SuppressFBWarnings({ "EI2" }) @Component @RequiredArgsConstructor
public class GetRequireEmail {

  public Mono<ResponseEntity<ResAPI>> example(Api api) {
    return new ResAPI(200).msg("").build();
  }
}
