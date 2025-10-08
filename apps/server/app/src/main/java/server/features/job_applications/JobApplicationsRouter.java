package server.features.job_applications;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.decorators.flow.api.Api;
import server.decorators.flow.res_api.ResAPI;
import server.features.job_applications.controllers.GetJobApplications;
import server.features.job_applications.controllers.PostJobApplications;
import server.features.job_applications.controllers.PutJobApplications;
import server.features.job_applications.controllers.PatchJobApplications;
import server.features.job_applications.controllers.DelJobApplications;
import server.router.RouterAPI;

@SuppressFBWarnings({ "EI2" }) @RouterAPI("/api/v1/job-applications") @RequiredArgsConstructor @SuppressWarnings({
    "unused", })
public class JobApplicationsRouter {
  private final GetJobApplications getCtrl;
  private final PostJobApplications postCtrl;
  private final PutJobApplications putCtrl;
  private final PatchJobApplications patchCtrl;
  private final DelJobApplications delCtrl;

  @PostMapping
  public Mono<ResponseEntity<ResAPI>> create(Api api) {
    return postCtrl.create(api);
  }

}
