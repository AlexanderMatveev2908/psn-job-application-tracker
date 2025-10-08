package server.features.job_applications.middleware;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.server.WebFilterChain;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.decorators.flow.ErrAPI;
import server.decorators.flow.api.Api;
import server.middleware.base_mdw.BaseMdw;
import server.models.applications.svc.JobApplRepo;
import server.paperwork.job_application.JobApplForm;

@Component @RequiredArgsConstructor
public class PutJobApplMdw extends BaseMdw {

  private final JobApplRepo jobRepo;

  @Override
  public Mono<Void> handle(Api api, WebFilterChain chain) {
    return isSubPathOf(api, chain, "/job-applications", HttpMethod.PUT, () -> {

      if (!api.hasPathUUID())
        return Mono.error(new ErrAPI("invalid job application id", 400));

      return checkMultipartForm(api, JobApplForm.class)
          .then(jobRepo.findById(api.getPathVarId().get())
              .switchIfEmpty(Mono.error(new ErrAPI("job application not found", 404)))
              .flatMap(existing -> !existing.getUserId().equals(api.getUser().getId())
                  ? Mono.error(new ErrAPI("forbidden", 403))
                  : chain.filter(api)));
    });
  }
}