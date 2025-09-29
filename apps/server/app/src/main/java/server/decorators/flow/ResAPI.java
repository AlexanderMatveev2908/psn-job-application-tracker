package server.decorators.flow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.Data;
import reactor.core.publisher.Mono;
import server.decorators.messages.ActT;
import server.decorators.messages.MapperMsg;

@Data
@JsonSerialize(using = ResApiJson.class)
public final class ResAPI {
    private String msg;
    private Integer status;
    private Map<String, Object> data;
    private final List<ResponseCookie> cookies = new ArrayList<>();

    public ResAPI(int status, String msg, Map<String, Object> data) {
        this.status = status;
        this.msg = msg;
        this.data = (data == null) ? null : Map.copyOf(data);
    }

    public ResAPI(int status) {
        this.status = status;
    }

    public ResAPI() {
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

    public ResAPI status(int status) {
        this.status = status;
        return this;
    }

    public ResAPI msg(String msg) {
        this.msg = msg;
        return this;
    }

    public ResAPI data(Map<String, Object> data) {
        this.data = data;
        return this;
    }

    public ResAPI cookie(ResponseCookie cookie) {
        this.cookies.add(cookie);
        return this;
    }

    public Mono<ResponseEntity<ResAPI>> build() {
        String safeMsg = (msg == null) ? MapperMsg.fromCode(status).getMsg() : msg;
        ActT act = (status >= 200 && status < 300) ? ActT.OK : ActT.ERR;

        ResponseEntity.BodyBuilder builder = ResponseEntity.status(status);
        for (ResponseCookie cookie : cookies)
            builder.header(HttpHeaders.SET_COOKIE, cookie.toString());

        return Mono.just(builder.body(
                new ResAPI()
                        .status(status)
                        .msg(prependEmj(safeMsg, act))
                        .data(data)));
    }
}
