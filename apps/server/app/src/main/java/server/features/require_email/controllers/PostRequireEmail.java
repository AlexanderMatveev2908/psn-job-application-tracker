package server.features.require_email.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.decorators.flow.Api;
import server.decorators.flow.ResAPI;
import server.features.require_email.services.RequireConfEmailSvc;

@SuppressFBWarnings({ "EI2" }) @Component @RequiredArgsConstructor
public class PostRequireEmail {

  private final RequireConfEmailSvc requireConfEmailSvc;

  public Mono<ResponseEntity<ResAPI>> confirmEmail(Api api) {
    return requireConfEmailSvc.mng(api).then(new ResAPI(200).msg("email sent").build());

  }
}
