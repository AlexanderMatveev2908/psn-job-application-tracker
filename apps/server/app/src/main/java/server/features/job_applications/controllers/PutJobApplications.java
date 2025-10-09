package server.features.job_applications.controllers;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.decorators.flow.api.Api;
import server.decorators.flow.res_api.ResAPI;
import server.features.job_applications.services.PutJobApplSvc;

@SuppressFBWarnings({ "EI2" })
@Component
@RequiredArgsConstructor
public class PutJobApplications {

  private final PutJobApplSvc putSvc;

  public Mono<ResponseEntity<ResAPI>> putApplication(Api api) {

    return putSvc.mng(api).flatMap(updated -> {
      return new ResAPI(200).msg("application updated").data(Map.of("jobApplication", updated)).build();
    });
  }
}
