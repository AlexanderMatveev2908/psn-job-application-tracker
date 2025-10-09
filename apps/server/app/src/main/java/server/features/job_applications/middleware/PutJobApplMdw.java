package server.features.job_applications.middleware;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.server.WebFilterChain;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.decorators.flow.ErrAPI;
import server.decorators.flow.api.Api;
import server.middleware.base_mdw.BaseMdw;
import server.models.applications.JobAppl;
import server.models.applications.svc.JobApplRepo;
import server.paperwork.job_application.JobApplForm;

@Component
@RequiredArgsConstructor
@SuppressFBWarnings({ "EI2" })
public class PutJobApplMdw extends BaseMdw {

  private final JobApplRepo jobRepo;

  @Override
  public Mono<Void> handle(Api api, WebFilterChain chain) {
    return isSubPathOf(api, chain, "/job-applications", HttpMethod.PUT, () -> {

      return limit(api, 15, 15).then(withPathId(api).flatMap(jobId -> checkMultipartForm(api, JobApplForm.class)
          .then(jobRepo.findById(jobId)
              .switchIfEmpty(Mono.error(new ErrAPI("job application not found", 404)))
              .flatMap(existing -> !existing.getUserId().equals(api.getUser().getId())
                  ? Mono.error(new ErrAPI("forbidden", 403))
                  : Mono.defer(() -> {
                    api.setMappedDataAttr(JobAppl.fromAttrApi(api));
                    return chain.filter(api);
                  })))));
    });
  }
}