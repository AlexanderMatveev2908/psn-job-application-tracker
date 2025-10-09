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

@SuppressFBWarnings({ "EI2" })
@Component
@RequiredArgsConstructor
public class GetJobApplications {

  public Mono<ResponseEntity<ResAPI>> getById(Api api) {

    Map<String, Object> body = new HashMap<>();
    body.put("jobApplication", api.getMappedData());

    return new ResAPI(200).msg("application found").data(body).build();
  }
}
