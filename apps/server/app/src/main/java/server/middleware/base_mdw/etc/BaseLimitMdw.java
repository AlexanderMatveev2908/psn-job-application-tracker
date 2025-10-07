package server.middleware.base_mdw.etc;

import java.util.Map;

import reactor.core.publisher.Mono;
import server.decorators.flow.api.Api;
import server.middleware.security.RateLimit;

public interface BaseLimitMdw {
  RateLimit useLimit();

  Mono<Map<String, Object>> grabBody(Api api);

  default Mono<Void> limit(Api api, int limit, int minutes) {
    return useLimit().limit(api, limit, minutes);
  }

  default Mono<Void> limit(Api api) {
    return limit(api, 5, 15);
  }

  default Mono<Map<String, Object>> limitAndRef(Api api, int limit, int minutes) {
    return limit(api, limit, minutes).then(grabBody(api));
  }

  default Mono<Map<String, Object>> limitAndRef(Api api) {
    return limitAndRef(api, 5, 15);
  }
}
