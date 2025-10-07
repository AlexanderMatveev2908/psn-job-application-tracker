package server.decorators.flow.res_api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.Getter;
import reactor.core.publisher.Mono;
import server.decorators.flow.res_api.etc.ResApiJson;
import server.decorators.messages.ActT;
import server.decorators.messages.MapperMsg;

@SuppressFBWarnings({ "EI" }) @Getter @JsonSerialize(using = ResApiJson.class)
public final class ResAPI {
    private String msg;
    private Integer status;
    private Map<String, Object> data;
    private final List<ResponseCookie> cookies = new ArrayList<>();
    private final List<ResponseCookie> deleteCookies = new ArrayList<>();

    public ResAPI(int status, String msg, Map<String, Object> data) {
        this.status = status;
        this.msg = msg;
        this.data = (data == null) ? null : Collections.unmodifiableMap(new LinkedHashMap<>(data));
    }

    public ResAPI(int status) {
        this.status = status;
    }

    public ResAPI() {
    }

    private static String prependEmj(String msg, ActT act) {
        return msg == null ? null : String.format("%s %s", act.getEmj(), msg.replace("❌ ", ""));
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
        return data == null ? null : new LinkedHashMap<>(data);
    }

    public List<ResponseCookie> getCookies() {
        return List.copyOf(cookies);
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
        this.data = (data == null) ? null : Collections.unmodifiableMap(new LinkedHashMap<>(data));
        return this;
    }

    public ResAPI cookie(ResponseCookie cookie) {
        this.cookies.add(cookie);
        return this;
    }

    public ResAPI delCookie(ResponseCookie cookie) {
        this.deleteCookies.add(cookie);
        return this;
    }

    public Mono<ResponseEntity<ResAPI>> build() {
        String safeMsg = status == 204 ? null : msg != null ? msg : MapperMsg.fromCode(status).getMsg();
        ActT act = (status >= 200 && status < 300) ? ActT.OK : ActT.ERR;

        ResponseEntity.BodyBuilder builder = ResponseEntity.status(status);
        for (ResponseCookie cookie : cookies)
            builder.header(HttpHeaders.SET_COOKIE, cookie.toString());

        for (ResponseCookie cookie : deleteCookies)
            builder.header(HttpHeaders.SET_COOKIE, cookie.toString());

        if (status == 204)
            return Mono.just(builder.build());

        var myRes = new ResAPI().status(status).msg(prependEmj(safeMsg, act)).data(data);

        return Mono.just(builder.body(myRes));
    }
}
