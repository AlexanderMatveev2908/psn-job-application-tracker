package server.features.job_applications.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.decorators.flow.api.Api;
import server.decorators.flow.res_api.ResAPI;
import server.features.job_applications.services.DelJobApplSvc;

@SuppressFBWarnings({ "EI2" })
@Component
@RequiredArgsConstructor
public class DelJobApplications {

  private final DelJobApplSvc delSvc;

  public Mono<ResponseEntity<ResAPI>> delById(Api api) {
    return delSvc.mng(api).then(new ResAPI(200).msg("application deleted").build());
  }
}
