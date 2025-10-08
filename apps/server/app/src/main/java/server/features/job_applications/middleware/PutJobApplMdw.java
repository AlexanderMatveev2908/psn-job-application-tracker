package server.features.job_applications.middleware;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.server.WebFilterChain;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.decorators.flow.api.Api;
import server.middleware.base_mdw.BaseMdw;

@Component @RequiredArgsConstructor
public class PutJobApplMdw extends BaseMdw {

  @Override
  public Mono<Void> handle(Api api, WebFilterChain chain) {
    return isTarget(api, chain, "/job-applications", HttpMethod.PUT, () -> {

      return chain.filter(api);
    });
  }
}