package server.features.job_applications.middleware;

import org.springframework.stereotype.Component;
import org.springframework.web.server.WebFilterChain;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.decorators.flow.api.Api;
import server.middleware.base_mdw.BaseMdw;
import server.models.applications.svc.JobCombo;

import org.springframework.http.HttpMethod;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

@Component
@RequiredArgsConstructor
@SuppressFBWarnings({ "EI2" })
public class DelApplMdw extends BaseMdw {

  private final JobCombo jobCombo;

  @Override
  public Mono<Void> handle(Api api, WebFilterChain chain) {
    return matchPath(api, chain, "/job-applications", HttpMethod.DELETE, () -> {
      return limit(api, 15, 15)
          .then(withPathId(api).flatMap(id -> jobCombo.existsAndBelongs(api, id).then(chain.filter(api))));
    });
  }
}
