package server.middleware.security;

import java.time.Duration;
import java.util.UUID;

import org.springframework.stereotype.Component;

import io.lettuce.core.Range;
import io.lettuce.core.api.reactive.RedisReactiveCommands;
import reactor.core.publisher.Mono;
import server.conf.db.remote_dictionary.RD;
import server.decorators.flow.Api;
import server.decorators.flow.ErrAPI;

@Component
public class RateLimit {
    private final RedisReactiveCommands<String, String> cmd;

    public RateLimit(RD rd) {
        this.cmd = rd.getCmd();

    }

    private String getClientIp(Api api) {
        String xff = api.getHeader("x-forwarded-for");

        if (xff != null && !xff.isBlank())
            return xff.split(",")[0].trim();

        return api.getIp();
    }

    public Mono<Void> limit(Api api, int limit, int minutes) {
        long now = System.currentTimeMillis();
        long windowMs = Duration.ofMinutes(minutes).toMillis();

        String ip = getClientIp(api);
        String path = api.getPath();
        String method = api.getMethod().toString();

        String key = String.format("rl:%s:%s__%s", ip, path, method);
        String val = now + ":" + UUID.randomUUID();

        return cmd.zremrangebyscore(key, Range.create(0, now - windowMs))
                .then(cmd.zadd(key, now, val))
                .then(cmd.zcard(key))
                .flatMap(count -> cmd.pexpire(key, windowMs + 1).thenReturn(count))
                .flatMap(count -> {
                    int remaining = Math.max(0, limit - count.intValue());

                    // ? method itself use String.valueOf on 2 arg
                    api.addHeader("RateLimit-Limit", limit);
                    api.addHeader("RateLimit-Remaining", remaining);
                    api.addHeader("RateLimit-Window", windowMs);

                    if (count < limit)
                        return Mono.empty();

                    return cmd.zrangeWithScores(key, 0, 0)
                            .singleOrEmpty()
                            .flatMap(tuple -> {
                                long oldest = (long) tuple.getScore();
                                long resetMs = Math.max(0, (windowMs - (now - oldest)));
                                api.addHeader("RateLimit-Reset", resetMs);

                                return Mono.error(new ErrAPI(
                                        "🐹 Our hamster-powered server took a break — try again later!",
                                        429));
                            });
                });
    }
}
