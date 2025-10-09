package server.features.job_applications.middleware;

import org.springframework.stereotype.Component;
import org.springframework.web.server.WebFilterChain;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.decorators.flow.api.Api;
import server.middleware.base_mdw.BaseMdw;
import server.models.applications.svc.JobCombo;

import org.springframework.http.HttpMethod;

@Component
@RequiredArgsConstructor
@SuppressFBWarnings({ "EI2" })
public class GetApplByIdMdw extends BaseMdw {

  private final JobCombo jobCombo;

  @Override
  public Mono<Void> handle(Api api, WebFilterChain chain) {
    return matchPath(api, chain, "/job-applications", HttpMethod.GET, () -> {
      return limit(api, 30, 15)
          .then(withPathId(api).flatMap(jobId -> jobCombo.existsAndBelongs(api, jobId).then(chain.filter(api))));
    });
  }
}