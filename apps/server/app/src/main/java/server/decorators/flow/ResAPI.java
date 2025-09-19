package server.decorators.flow;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import server.decorators.messages.ActT;
import server.decorators.messages.MapperMsg;

public class ResAPI<T> {
    public final String msg;
    public final Integer status;
    public final T data;

    public ResAPI(String msg, int status, T data) {
        this.msg = msg;
        this.status = status;
        this.data = data;
    }

    // ? core
    private static <T> ResponseEntity<ResAPI<T>> genRes(int code, String msg, T data) {
        String safeMsg = msg == null ? MapperMsg.fromCode(code).getMsg() : msg;

        ActT act = (code >= 200 && code < 300)
                ? ActT.OK
                : ActT.ERR;

        return ResponseEntity
                .status(code)
                .body(new ResAPI<>(prependEmj(safeMsg, act), code, data));
    }

    // ? helpers
    public static String prependEmj(String msg, ActT act) {
        return String.format("%s %s", act.getEmj(), msg);
    }

    // ? ok
    public static <T> ResponseEntity<ResAPI<T>> ok200(String msg, T data) {
        return genRes(200, msg, data);
    }

    public static <T> ResponseEntity<ResAPI<T>> ok200(T data) {
        return ok200(null, data);
    }

    public static <T> ResponseEntity<ResAPI<T>> ok201(String msg, T data) {
        return genRes(201, msg, data);
    }

    public static <T> ResponseEntity<ResAPI<T>> ok201(T data) {
        return ok201(null, data);
    }

    public static <T> ResponseEntity<Void> ok204() {
        return ResponseEntity.status(204).build();
    }

    // ? err
    public static ResponseEntity<ResAPI<Void>> err400(String msg) {
        return genRes(400, msg, null);
    }

    public static ResponseEntity<ResAPI<Void>> err401(String msg) {
        return genRes(401, msg, null);
    }

    public static ResponseEntity<ResAPI<Void>> err403(String msg) {
        return genRes(403, msg, null);
    }

    public static ResponseEntity<ResAPI<Void>> err404(String msg) {
        return genRes(404, msg, null);
    }

    public static ResponseEntity<ResAPI<Void>> err409(String msg) {
        return genRes(409, msg, null);
    }

    public static ResponseEntity<ResAPI<Void>> err422(String msg) {
        return genRes(422, msg, null);
    }

    public static ResponseEntity<ResAPI<Void>> err500() {
        return genRes(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "A wild slime appeared! Server takes damage of 500 hp ⚔️", null);
    }
}
