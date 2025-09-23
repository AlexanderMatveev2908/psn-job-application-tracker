package server.conf.db;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

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
    private final ObjectMapper jack = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

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
                .onErrorMap(err -> new ErrAPI("❌ rd cnt failed", 500))
                .map(res -> {
                    if (!"PONG".equals(res))
                        throw new ErrAPI("❌ rd cnt failed", 500);

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
                .onErrorMap(err -> new ErrAPI("❌ rd fetch stats failed", 500));
    }

    private String toJson(Object obj) {
        try {
            return jack.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    public Mono<Integer> dbSize() {
        return cmd.dbsize()
                .onErrorMap(err -> new ErrAPI("❌ rd fetch db size failed", 500))
                .map(size -> {
                    System.out.println(String.format("🗃️ rd db size => %d", size.intValue()));
                    return size.intValue();
                });
    }

    public Mono<Object> grabAll() {
        return cmd.keys("*")
                .flatMap(key -> cmd.type(key).flatMap(type -> switch (type.toLowerCase()) {
                    case "string" -> cmd.get(key)
                            .map(val -> Map.entry(key, val));

                    case "hash" -> cmd.hgetall(key)
                            .collectMap(kv -> kv.getKey(), kv -> kv.getValue())
                            .map(map -> Map.entry(key, toJson(map)));

                    case "list" -> cmd.lrange(key, 0, -1)
                            .collectList()
                            .map(list -> Map.entry(key, toJson(list)));

                    case "set" -> cmd.smembers(key)
                            .collectList()
                            .map(list -> Map.entry(key, toJson(list)));

                    case "zset" -> cmd.zrange(key, 0, -1)
                            .collectList()
                            .map(list -> Map.entry(key, toJson(list)));

                    default -> Mono.empty();
                }))
                .collectMap(Map.Entry::getKey, Map.Entry::getValue)
                .map(res -> {
                    System.out.println("🗃️ rd cache => ");
                    res.forEach((k, v) -> System.out.println(String.format("🔑 %s => 🖍️ %s", k, v)));
                    return res;
                });
    }

    public Mono<String> flushAll() {
        return cmd.flushall()
                .map(res -> {
                    if (!"OK".equals(res))
                        throw new ErrAPI("❌ rd flush all failed", 500);

                    System.out.println("🔪 rd cleaned");

                    return res;
                })
                .onErrorMap(err -> new ErrAPI("❌ rd flush all failed", 500));
    }

    public void close() {
        if (cnt != null && cnt.isOpen())
            cnt.close();

        if (client != null)
            client.shutdown();
    }

}
