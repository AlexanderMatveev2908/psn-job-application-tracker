package server.decorators.flow;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.Data;
import reactor.core.publisher.Mono;
import server.decorators.messages.ActT;
import server.decorators.messages.MapperMsg;

@Data
@JsonSerialize(using = ResApiJson.class)
public final class ResAPI {
    private final String msg;
    private final Integer status;
    private final Map<String, Object> data;

    private static String prependEmj(String msg, ActT act) {
        return String.format("%s %s", act.getEmj(), msg);
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> flatData(Map<String, Object> data) {
        Map<String, Object> flatten = new HashMap<>();
        if (data == null) {
            flatten.put("data", null);
        } else if (data instanceof Map<?, ?> map) {
            map.forEach((k, v) -> flatten.put(String.valueOf(k), v));
        } else {
            Map<String, Object> map = new ObjectMapper().convertValue(data, Map.class);
            flatten.putAll(map);
        }

        return flatten;
    }

    public static Mono<ResponseEntity<ResAPI>> of(int code, String msg, Map<String, Object> data) {
        String safeMsg = (msg == null) ? MapperMsg.fromCode(code).getMsg() : msg;
        ActT act = (code >= 200 && code < 300) ? ActT.OK : ActT.ERR;

        return Mono.just(
                ResponseEntity
                        .status(code)
                        .body(new ResAPI(prependEmj(safeMsg, act), code, data)));
    }

    public static Mono<ResponseEntity<ResAPI>> ok200(String msg, Map<String, Object> data) {
        return of(200, msg, data);
    }

    public static Mono<ResponseEntity<ResAPI>> ok200(Map<String, Object> data) {
        return of(200, null, data);
    }

    public static Mono<ResponseEntity<ResAPI>> ok200(String msg) {
        return of(200, msg, null);
    }

    public static Mono<ResponseEntity<ResAPI>> ok201(String msg, Map<String, Object> data) {
        return of(201, msg, data);
    }

    public static Mono<ResponseEntity<ResAPI>> ok201(Map<String, Object> data) {
        return ok201(null, data);
    }

    public static Mono<ResponseEntity<ResAPI>> ok204() {
        return Mono.fromSupplier(() -> ResponseEntity.status(204).build());
    }

    public static Mono<ResponseEntity<ResAPI>> err400(String msg) {
        return of(400, msg, null);
    }

    public static Mono<ResponseEntity<ResAPI>> err401(String msg) {
        return of(401, msg, null);
    }

    public static Mono<ResponseEntity<ResAPI>> err403(String msg) {
        return of(403, msg, null);
    }

    public static Mono<ResponseEntity<ResAPI>> err404(String msg) {
        return of(404, msg, null);
    }

    public static Mono<ResponseEntity<ResAPI>> err409(String msg) {
        return of(409, msg, null);
    }

    public static Mono<ResponseEntity<ResAPI>> err422(String msg) {
        return of(422, msg, null);
    }

    public static Mono<ResponseEntity<ResAPI>> err500() {
        return of(
                500,
                "❌ A wild slime appeared! Server takes damage of 500 hp ⚔️",
                null);
    }

}
