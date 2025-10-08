package server.middleware.base_mdw.etc.interfaces;

import java.util.Map;

import reactor.core.publisher.Mono;
import server.decorators.flow.api.Api;
import server.middleware.base_mdw.etc.services_mdw.RateLimitSvcMdw;

public interface BaseLimitMdw {
  RateLimitSvcMdw useLimit();

  Mono<Map<String, Object>> grabBody(Api api);

  default Mono<Void> limit(Api api, int limit, int minutes) {
    return useLimit().limit(api, limit, minutes);
  }

}
