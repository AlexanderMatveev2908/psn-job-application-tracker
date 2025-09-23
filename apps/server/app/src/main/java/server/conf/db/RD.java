package server.conf.db;

import org.springframework.stereotype.Service;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.reactive.RedisReactiveCommands;
import reactor.core.publisher.Mono;
import server.conf.env_conf.EnvKeeper;
import server.decorators.flow.ErrAPI;

@Service
public class RD {
    private final RedisClient client;
    private final StatefulRedisConnection<String, String> cnt;
    private final RedisReactiveCommands<String, String> cmd;

    public RD(EnvKeeper envKeeper) {
        this.client = RedisClient.create(envKeeper.get("redisUrl"));
        this.cnt = client.connect();
        this.cmd = cnt.reactive();
    }

    public RedisClient getClient() {
        return client;
    }

    public StatefulRedisConnection<String, String> getCnt() {
        return cnt;
    }

    public RedisReactiveCommands<String, String> getCmd() {
        return cmd;
    }

    public Mono<String> checkConnection() {
        return cmd.ping()
                .onErrorMap(err -> new ErrAPI("❌ Redis cnt failed", 500))
                .map(res -> {
                    if (!"PONG".equals(res))
                        throw new ErrAPI("❌ Redis cnt failed", 500);

                    return res;
                });
    }

}
