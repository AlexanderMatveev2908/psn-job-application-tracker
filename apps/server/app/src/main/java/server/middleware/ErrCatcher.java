package server.middleware;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import server.decorators.ErrAPI;
import server.lib.dev.MyLog;

@SuppressWarnings("UseSpecificCatch")

@RestControllerAdvice
public class ErrCatcher {

    private final MyLog log;

    public ErrCatcher(MyLog log) {
        this.log = log;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(Exception err) {
        Map<String, Object> body = new LinkedHashMap<>();

        body.put("msg", err.getMessage());
        body.put("status", err instanceof ErrAPI ? ((ErrAPI) err).status : 500);

        log.logErr(err);

        return ResponseEntity
                .status((int) body.get("status"))
                .body(body);
    }
}