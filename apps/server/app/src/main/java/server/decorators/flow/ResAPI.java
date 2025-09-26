package server.decorators.flow;

import org.springframework.http.ResponseEntity;

import reactor.core.publisher.Mono;
import server.decorators.messages.ActT;
import server.decorators.messages.MapperMsg;

public final class ResAPI<T> {
    public final String msg;
    public final Integer status;
    public final T data;

    public ResAPI(String msg, int status, T data) {
        this.msg = msg;
        this.status = status;
        this.data = data;
    }

    private static String prependEmj(String msg, ActT act) {
        return String.format("%s %s", act.getEmj(), msg);
    }

    public static <T> Mono<ResponseEntity<ResAPI<T>>> of(int code, String msg, T data) {
        String safeMsg = (msg == null) ? MapperMsg.fromCode(code).getMsg() : msg;
        ActT act = (code >= 200 && code < 300) ? ActT.OK : ActT.ERR;

        return Mono.just(
                ResponseEntity
                        .status(code)
                        .body(new ResAPI<>(prependEmj(safeMsg, act), code, data)));
    }

    public static <T> Mono<ResponseEntity<ResAPI<T>>> ok200(String msg, T data) {
        return of(200, msg, data);
    }

    public static <T> Mono<ResponseEntity<ResAPI<T>>> ok200(T data) {
        return ok200(null, data);
    }

    public static Mono<ResponseEntity<ResAPI<Object>>> ok200(String msg) {
        return of(200, msg, null);
    }

    public static <T> Mono<ResponseEntity<ResAPI<T>>> ok201(String msg, T data) {
        return of(201, msg, data);
    }

    public static <T> Mono<ResponseEntity<ResAPI<T>>> ok201(T data) {
        return ok201(null, data);
    }

    public static Mono<ResponseEntity<ResAPI<Void>>> ok204() {
        return Mono.fromSupplier(() -> ResponseEntity.status(204).build());
    }

    public static Mono<ResponseEntity<ResAPI<Object>>> err400(String msg) {
        return of(400, msg, null);
    }

    public static Mono<ResponseEntity<ResAPI<Object>>> err401(String msg) {
        return of(401, msg, null);
    }

    public static Mono<ResponseEntity<ResAPI<Object>>> err403(String msg) {
        return of(403, msg, null);
    }

    public static Mono<ResponseEntity<ResAPI<Object>>> err404(String msg) {
        return of(404, msg, null);
    }

    public static Mono<ResponseEntity<ResAPI<Object>>> err409(String msg) {
        return of(409, msg, null);
    }

    public static Mono<ResponseEntity<ResAPI<Object>>> err422(String msg) {
        return of(422, msg, null);
    }

    public static Mono<ResponseEntity<ResAPI<Object>>> err500() {
        return of(
                500,
                "❌ A wild slime appeared! Server takes damage of 500 hp ⚔️",
                null);
    }
}
