package server.conf.db;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.reactive.RedisReactiveCommands;
import reactor.core.publisher.Mono;
import server.conf.env_conf.EnvKeeper;
import server.decorators.RootCls;
import server.decorators.flow.ErrAPI;

@Service
public class RD implements RootCls {
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
                .onErrorMap(err -> new ErrAPI("❌ RD cnt failed", 500))
                .map(res -> {
                    if (!"PONG".equals(res))
                        throw new ErrAPI("❌ RD cnt failed", 500);

                    return res;
                });
    }

    public Mono<Map<String, Object>> stats() {
        return cmd.info()
                .map(info -> {
                    Map<String, Object> parsed = new HashMap<>();

                    String[] lines = info.split("\n");
                    for (String ln : lines) {
                        ln = ln.trim();
                        if (ln.isEmpty() || ln.startsWith("#"))
                            continue;

                        String[] kv = ln.split(":", 2);
                        if (kv.length == 2)
                            parsed.put(kv[0], kv[1]);

                    }

                    for (Map.Entry<String, Object> en : parsed.entrySet())
                        System.out.println(String.format("📊 %s => %s", en.getKey(), en.getValue().toString()));

                    return parsed;
                })
                .onErrorMap(err -> new ErrAPI("❌ RD fetch stats failed", 500));
    }

    public void close() {
        if (cnt != null && cnt.isOpen())
            cnt.close();

        if (client != null)
            client.shutdown();
    }

}
