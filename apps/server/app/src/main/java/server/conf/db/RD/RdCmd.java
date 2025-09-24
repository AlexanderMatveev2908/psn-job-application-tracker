package server.conf.db.RD;

import java.util.Map;

import org.springframework.stereotype.Service;

import io.lettuce.core.api.reactive.RedisReactiveCommands;
import reactor.core.publisher.Mono;
import server.decorators.flow.ErrAPI;
import server.lib.dev.MyLog;
import server.lib.etc.Frmt;

@Service
public class RdCmd {
    private final RedisReactiveCommands<String, String> cmd;

    public RdCmd(RD rd) {
        this.cmd = rd.getCmd();
    }

    public Mono<String> setStr(String key, String value) {
        return cmd.set(key, value);
    }

    public Mono<String> getStr(String key) {
        return cmd.get(key)
                .switchIfEmpty(Mono.error(new ErrAPI("❌ key not found => " + key, 404)))
                .doOnNext(val -> MyLog.logKV(key, val));
    }

    public Mono<Integer> delK(String key) {
        return cmd.del(key)
                .flatMap(v -> {
                    if (v == 0)
                        return Mono.error(new ErrAPI("key not found => " + key, 404));

                    return Mono.just(v.intValue());
                })
                .doOnNext(v -> System.out.println("🔪 deleted " + v + " key"));

    }

    public Mono<Object> grabAll() {
        return cmd.keys("*")
                .flatMap(key -> cmd.type(key).flatMap(type -> switch (type.toLowerCase()) {
                    case "string" -> cmd.get(key)
                            .map(val -> Map.entry(key, val));

                    case "hash" -> cmd.hgetall(key)
                            .collectMap(kv -> kv.getKey(), kv -> kv.getValue())
                            .map(map -> Map.entry(key, Frmt.toJson(map)));

                    case "list" -> cmd.lrange(key, 0, -1)
                            .collectList()
                            .map(list -> Map.entry(key, Frmt.toJson(list)));

                    case "set" -> cmd.smembers(key)
                            .collectList()
                            .map(list -> Map.entry(key, Frmt.toJson(list)));

                    case "zset" -> cmd.zrange(key, 0, -1)
                            .collectList()
                            .map(list -> Map.entry(key, Frmt.toJson(list)));

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
}
