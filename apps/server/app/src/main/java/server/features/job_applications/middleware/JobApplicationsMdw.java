package server.features.job_applications.middleware;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.WebFilterChain;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.decorators.flow.api.Api;
import server.middleware.base_mdw.BaseMdw;

@SuppressFBWarnings({ "EI2" })
@Component
@RequiredArgsConstructor
@Order(50)
public class JobApplicationsMdw extends BaseMdw {

  @Override
  public Mono<Void> handle(Api api, WebFilterChain chain) {
    return isSubPathOf(api, chain, "/job-applications", () -> {
      return checkJwtVerified(api).then(chain.filter(api));
    });
  }
}
