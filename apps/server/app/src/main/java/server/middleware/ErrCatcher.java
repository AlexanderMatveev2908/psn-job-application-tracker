package server.middleware;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;
import server.decorators.flow.ErrAPI;
import server.decorators.flow.ResAPI;
import server.lib.dev.MyLog;

@RestControllerAdvice
public class ErrCatcher {

    @ExceptionHandler(ResponseStatusException.class)
    public Mono<ResponseEntity<ResAPI<Object>>> handleResponseStatus(
            ResponseStatusException err, ServerWebExchange exc) {

        if (err.getStatusCode() != HttpStatus.NOT_FOUND ||
                err.getMessage() == null ||
                !err.getMessage().contains("NOT_FOUND"))
            return handleGeneric(err, exc);

        MyLog.logErr(err);

        String path = exc.getRequest().getPath().value();
        return ResAPI.err404(String.format("%s not found 🚦", path));

    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<ResAPI<Object>>> handleGeneric(Exception err, ServerWebExchange exc) {

        MyLog.logErr(err);

        String msg = err.getMessage();
        int status = (err instanceof ErrAPI) ? ((ErrAPI) err).getStatus() : 500;
        Object data = (err instanceof ErrAPI) ? ((ErrAPI) err).getData() : null;

        return ResAPI.of(status, msg, data);
    }
}
