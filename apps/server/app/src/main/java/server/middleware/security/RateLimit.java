// package server.middleware.security;

// import java.time.Duration;
// import java.util.UUID;

// import org.springframework.http.HttpStatus;
// import org.springframework.stereotype.Component;
// import org.springframework.web.server.ServerWebExchange;
// import org.springframework.web.server.WebFilter;
// import org.springframework.web.server.WebFilterChain;

// import io.lettuce.core.Range;
// import io.lettuce.core.api.reactive.RedisReactiveCommands;
// import reactor.core.publisher.Mono;
// import server.decorators.flow.Api;
// import server.decorators.flow.ErrAPI;

// @Component
// public class RateLimit implements WebFilter {
// private final RedisReactiveCommands<String, String> redis;

// public RateLimit(RedisReactiveCommands<String, String> redis) {
// this.redis = redis;
// }

// private String getClientIp(Api api) {
// String xff = api.getHeader("x-forwarded-for");

// if (xff != null && !xff.isBlank())
// return xff.split(",")[0].trim();

// return api.getIp();
// }

// @Override
// public Mono<Void> filter(ServerWebExchange exc, WebFilterChain chain) {
// int limit = 5;
// long windowMs = Duration.ofMinutes(1).toMillis();
// long now = System.currentTimeMillis();

// var api = new Api(exc);

// String ip = getClientIp(api);
// String path = api.getPath();
// String method = api.getMethod().toString();

// String key = String.format("rl:%s:%s__%s", ip, path, method);
// String val = now + ":" + UUID.randomUUID();

// return redis.zremrangebyscore(key, Range.create(0, now - windowMs))
// .then(redis.zadd(key, now, val))
// .then(redis.zcard(key))
// .flatMap(count -> redis.expire(key, (windowMs / 1000) + 1).thenReturn(count))
// .flatMap(count -> {
// int remaining = Math.max(0, limit - count.intValue());

// exc.getResponse().getHeaders().add("RateLimit-Limit", String.valueOf(limit));
// exc.getResponse().getHeaders().add("RateLimit-Remaining",
// String.valueOf(remaining));
// exc.getResponse().getHeaders().add("RateLimit-Window",
// String.valueOf(windowMs));

// if (count > limit) {
// return redis.zrangeWithScores(key, 0, 0)
// .singleOrEmpty()
// .flatMap(tuple -> {
// long oldest = (long) tuple.getScore();
// long resetMs = Math.max(0, (windowMs - (now - oldest)));
// exc.getResponse().getHeaders().add("RateLimit-Reset",
// String.valueOf(resetMs));

// return Mono.error(new ErrAPI(
// "🐹 Our hamster-powered server took a break — try again later!",
// HttpStatus.TOO_MANY_REQUESTS.value()));
// });
// }

// return chain.filter(exc);
// });

// }
// }
