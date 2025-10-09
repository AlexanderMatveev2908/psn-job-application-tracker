package server.features.job_applications.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.decorators.flow.api.Api;
import server.decorators.flow.res_api.ResAPI;
import server.features.job_applications.services.ReadJobsSvc;

@SuppressFBWarnings({ "EI2" })
@Component
@RequiredArgsConstructor
public class GetJobApplications {

  private final ReadJobsSvc readSvc;

  public Mono<ResponseEntity<ResAPI>> getById(Api api) {
    Map<String, Object> body = new HashMap<>();
    body.put("jobApplication", api.getCurrApplication());
    return new ResAPI(200).msg("application found").data(body).build();
  }

  public Mono<ResponseEntity<ResAPI>> readAll(Api api) {
    return readSvc.mng(api).flatMap(res -> new ResAPI(200).msg("ok").data(res).build());
  }
}
