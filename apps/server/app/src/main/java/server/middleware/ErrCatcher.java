package server.middleware;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import server.decorators.ErrAPI;
import server.decorators.flow.ResAPI;
import server.decorators.messages.ActT;
import server.lib.dev.MyLog;

@SuppressWarnings("UseSpecificCatch")

@RestControllerAdvice
public class ErrCatcher {

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ResAPI<Void>> handleNotFound(NoHandlerFoundException err) {
        return ResAPI.err404(String.format("%s not supported", err.getRequestURL()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResAPI<Object>> handleGeneric(Exception err) {

        String msg = err.getMessage();
        int status = err instanceof ErrAPI ? ((ErrAPI) err).getStatus() : 500;
        Object data = err instanceof ErrAPI ? ((ErrAPI) err).getData() : null;

        MyLog.logErr(err);

        return ResponseEntity.status(status).body(new ResAPI<>(ResAPI.prependEmj(msg, ActT.ERR), status, data));
    }

}