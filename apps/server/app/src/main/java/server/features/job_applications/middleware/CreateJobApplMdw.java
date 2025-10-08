package server.features.job_applications.middleware;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.server.WebFilterChain;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.decorators.flow.api.Api;
import server.middleware.base_mdw.BaseMdw;
import server.paperwork.job_application.JobApplForm;

@Component @RequiredArgsConstructor
public class CreateJobApplMdw extends BaseMdw {

  @Override
  public Mono<Void> handle(Api api, WebFilterChain chain) {
    return isTarget(api, chain, "/job-applications", HttpMethod.POST, () -> {
      return limit(api, 15, 15).then(checkMultipartForm(api, JobApplForm.class)).then(chain.filter(api));
    });
  }
}
