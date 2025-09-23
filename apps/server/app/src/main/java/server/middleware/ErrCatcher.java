package server.middleware;

import java.nio.charset.StandardCharsets;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import reactor.core.publisher.Mono;
import server.decorators.flow.ErrAPI;
import server.decorators.flow.ResAPI;
import server.lib.dev.MyLog;

@Component
@Order(-1)
public class ErrCatcher implements WebExceptionHandler {

    private final ObjectMapper mapper = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT);

    @Override
    public Mono<Void> handle(ServerWebExchange exc, Throwable err) {
        String msg = err.getMessage();
        int status = (err instanceof ErrAPI) ? ((ErrAPI) err).getStatus() : 500;
        Object data = (err instanceof ErrAPI) ? ((ErrAPI) err).getData() : null;

        var res = exc.getResponse();
        res.setStatusCode(HttpStatus.valueOf(status));
        res.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        var apiBody = new ResAPI<>(msg, status, data);

        byte[] bytes;
        try {
            bytes = mapper.writeValueAsBytes(apiBody);
        } catch (JacksonException e) {
            bytes = ("{\"msg\":\"serialization failed\",\"status\":500,\"data\":null}")
                    .getBytes(StandardCharsets.UTF_8);
        }

        return res.writeWith(Mono.just(res.bufferFactory().wrap(bytes)));
    }

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
