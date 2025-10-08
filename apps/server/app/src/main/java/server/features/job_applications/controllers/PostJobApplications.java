package server.features.job_applications.controllers;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.decorators.flow.api.Api;
import server.decorators.flow.res_api.ResAPI;
import server.features.job_applications.services.CreateApplSvc;

@SuppressFBWarnings({ "EI2" }) @Component @RequiredArgsConstructor
public class PostJobApplications {

  private final CreateApplSvc createSvc;

  public Mono<ResponseEntity<ResAPI>> create(Api api) {

    return createSvc.mng(api).flatMap(saved -> {

      Map<String, Object> body = Map.of("application", saved);

      return new ResAPI(200).msg("application created").data(body).build();
    });
  }
}
