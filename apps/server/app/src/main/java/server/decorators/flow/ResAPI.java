package server.decorators.flow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;

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
    private final List<ResponseCookie> cookies;

    public ResAPI(String msg, Integer status, Map<String, Object> data, List<ResponseCookie> cookies) {
        this.msg = msg;
        this.status = status;
        this.data = (data == null) ? null : Map.copyOf(data);
        this.cookies = cookies;
    }

    public ResAPI(String msg, Integer status, Map<String, Object> data) {
        this.msg = msg;
        this.status = status;
        this.data = (data == null) ? null : Map.copyOf(data);
        this.cookies = null;
    }

    private static String prependEmj(String msg, ActT act) {
        return String.format("%s %s", act.getEmj(), msg);
    }

    public static Map<String, Object> flatData(Map<String, Object> data) {
        Map<String, Object> flatten = new HashMap<>();
        if (data == null)
            flatten.put("data", null);
        else
            flatten.putAll(data);

        return flatten;
    }

    public Map<String, Object> getData() {
        return data == null ? null : Map.copyOf(data);
    }

    public static class Builder {
        private int code = 200;
        private String msg;
        private Map<String, Object> data;
        private final List<ResponseCookie> cookies = new ArrayList<>();

        public Builder status(int code) {
            this.code = code;
            return this;
        }

        public Builder msg(String msg) {
            this.msg = msg;
            return this;
        }

        public Builder data(Map<String, Object> data) {
            this.data = data;
            return this;
        }

        public Builder cookie(ResponseCookie cookie) {
            this.cookies.add(cookie);
            return this;
        }

        public Mono<ResponseEntity<ResAPI>> build() {
            String safeMsg = (msg == null) ? MapperMsg.fromCode(code).getMsg() : msg;
            ActT act = (code >= 200 && code < 300) ? ActT.OK : ActT.ERR;

            ResponseEntity.BodyBuilder builder = ResponseEntity.status(code);
            for (ResponseCookie cookie : cookies)
                builder.header(HttpHeaders.SET_COOKIE, cookie.toString());

            return Mono.just(builder.body(new ResAPI(prependEmj(safeMsg, act), code, data)));
        }
    }

    public static Mono<ResponseEntity<ResAPI>> of(int code, String msg, Map<String, Object> data,
            List<ResponseCookie> cookies) {
        String safeMsg = (msg == null) ? MapperMsg.fromCode(code).getMsg() : msg;
        ActT act = (code >= 200 && code < 300) ? ActT.OK : ActT.ERR;

        BodyBuilder builder = ResponseEntity.status(code);

        if (cookies != null)
            for (ResponseCookie cookie : cookies)
                builder.header(HttpHeaders.SET_COOKIE, cookie.toString());

        return Mono.just(
                builder
                        .body(new ResAPI(prependEmj(safeMsg, act), code, data, cookies)));
    }

    public static Mono<ResponseEntity<ResAPI>> of(int code, String msg, Map<String, Object> data) {
        return of(code, msg, data);
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
