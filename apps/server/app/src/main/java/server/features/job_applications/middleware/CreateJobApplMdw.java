package server.features.job_applications.middleware;

import org.springframework.stereotype.Component;
import org.springframework.web.server.WebFilterChain;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.decorators.flow.api.Api;
import server.lib.dev.MyLog;
import server.middleware.base_mdw.BaseMdw;

@Component @RequiredArgsConstructor
public class CreateJobApplMdw extends BaseMdw {

  @Override
  public Mono<Void> handle(Api api, WebFilterChain chain) {
    return isTarget(api, chain, "/job-applications", () -> {

      MyLog.log(api.getUser());

      return chain.filter(api);
    });
  }
}
