package server.features.job_applications.middleware;

import org.springframework.stereotype.Component;
import org.springframework.web.server.WebFilterChain;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.decorators.flow.api.Api;
import server.middleware.base_mdw.BaseMdw;
import server.paperwork.job_application.read.QueryJobsForm;

import org.springframework.http.HttpMethod;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

@Component
@RequiredArgsConstructor
@SuppressFBWarnings({ "EI2" })
public class ReadJobApplMdw extends BaseMdw {

  @Override
  public Mono<Void> handle(Api api, WebFilterChain chain) {
    return isTarget(api, chain, "/job-applications", HttpMethod.GET, () -> {
      return limit(api, 50, 15).then(checkQueryForm(api, QueryJobsForm.class).then(chain.filter(api)));
    });
  }
}