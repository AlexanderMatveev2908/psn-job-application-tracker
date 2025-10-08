package server.features.job_applications.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.decorators.flow.api.Api;
import server.decorators.flow.res_api.ResAPI;

@SuppressFBWarnings({ "EI2" }) @Component @RequiredArgsConstructor
public class PostJobApplications {

  public Mono<ResponseEntity<ResAPI>> create(Api api) {
    return new ResAPI(200).msg("Post JobApplications endpoint").build();
  }
}
