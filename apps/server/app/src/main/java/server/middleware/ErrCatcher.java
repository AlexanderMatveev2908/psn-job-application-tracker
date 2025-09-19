package server.middleware;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import server.decorators.ActT;
import server.decorators.ErrAPI;
import server.decorators.ResAPI;
import server.lib.dev.MyLog;

@SuppressWarnings("UseSpecificCatch")

@RestControllerAdvice
public class ErrCatcher {

    private final MyLog log;

    public ErrCatcher(MyLog log) {
        this.log = log;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResAPI<Object>> handleGeneric(Exception err) {

        String msg = err.getMessage();
        int status = err instanceof ErrAPI ? ((ErrAPI) err).getStatus() : 500;
        Object data = err instanceof ErrAPI ? ((ErrAPI) err).getData() : null;

        log.logErr(err);

        return ResponseEntity.status(status).body(new ResAPI<>(ResAPI.prependEmj(msg, ActT.ERR), status, data));
    }
}