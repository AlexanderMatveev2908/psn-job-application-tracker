package server.middleware;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.decorators.flow.Api;
import server.decorators.flow.ErrAPI;
import server.middleware.security.RateLimit;

@Service
@RequiredArgsConstructor
public class CmnMdw {
    private final RateLimit rl;

    public Mono<Map<String, Object>> limitAndRef(Api api, int limit, int minutes) {

        return rl.limit(api, limit, minutes).then(api.getBd(new TypeReference<Map<String, Object>>() {
        })).switchIfEmpty(Mono.error(new ErrAPI("data not provided", 400)));
    }

    public Mono<Map<String, Object>> limitAndRef(Api api) {

        return rl.limit(api).then(api.getBd(new TypeReference<Map<String, Object>>() {
        })).switchIfEmpty(Mono.error(new ErrAPI("data not provided", 400)));
    }

    public Mono<Boolean> isTarget(Api api, String p) {
        return Mono.just(api.isSamePath("/api/v1" + p));
    }

}
