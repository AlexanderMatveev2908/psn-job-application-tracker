package server.middleware;

import java.util.Map;
import java.util.Optional;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import reactor.core.publisher.Mono;
import server.decorators.flow.ErrAPI;
import server.decorators.flow.ResAPI;
import server.lib.data_structure.Prs;
import server.lib.dev.MyLog;

@Component @Order(-1)
public class ErrCatcher implements WebExceptionHandler {

    private final ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    @Override
    public Mono<Void> handle(ServerWebExchange exc, Throwable err) {

        MyLog.logErr(err);

        String msg = Optional.ofNullable(err.getMessage()).orElse("");
        boolean isRouteNotFound = msg.equals("404 NOT_FOUND");
        if (isRouteNotFound) {
            String endpoint = exc.getRequest().getPath().value();
            msg = String.format("route %s not found 🚦", endpoint);
        }
        msg = String.format("%s %s", err instanceof ErrAPI ? "❌" : "💣", msg.replace("❌ ", ""));
        int status = (err instanceof ErrAPI) ? ((ErrAPI) err).getStatus() : isRouteNotFound ? 404 : 500;
        Map<String, Object> data = (err instanceof ErrAPI) ? ((ErrAPI) err).getData() : null;

        var res = exc.getResponse();
        res.setStatusCode(HttpStatus.valueOf(status));
        res.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        var apiBody = new ResAPI(status, msg, data);

        byte[] bytes;
        try {
            bytes = mapper.writeValueAsBytes(apiBody);
        } catch (JacksonException e) {
            bytes = Prs.utf8ToBinary("{\"msg\":\"serialization failed\",\"status\":500,\"data\":null}");

        }

        return res.writeWith(Mono.just(res.bufferFactory().wrap(bytes)));
    }

}
