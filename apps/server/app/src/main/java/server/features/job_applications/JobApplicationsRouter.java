package server.features.job_applications;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

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

@SuppressFBWarnings({ "EI2" })
@RouterAPI("/api/v1/job-applications")
@RequiredArgsConstructor
@SuppressWarnings({
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

  @GetMapping("/{applicationId}")
  public Mono<ResponseEntity<ResAPI>> getById(Api api) {
    return getCtrl.getById(api);
  }

  @PutMapping("/{applicationId}")
  public Mono<ResponseEntity<ResAPI>> putApplication(Api api) {
    return putCtrl.putApplication(api);
  }

  @DeleteMapping("/{applicationId}")
  public Mono<ResponseEntity<ResAPI>> delById(Api api) {
    return delCtrl.delById(api);
  }

  @GetMapping
  public Mono<ResponseEntity<ResAPI>> readAll(Api api) {
    return getCtrl.readAll(api);
  }
}
