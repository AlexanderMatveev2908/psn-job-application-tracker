package server.features.job_applications.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.decorators.flow.api.Api;
import server.decorators.flow.res_api.ResAPI;

@SuppressFBWarnings({ "EI2" }) 
@Component 
@RequiredArgsConstructor
public class PatchJobApplications {

  public Mono<ResponseEntity<ResAPI>> example(Api api) {
    return new ResAPI(200).msg("Patch JobApplications endpoint").build();
  }
}
