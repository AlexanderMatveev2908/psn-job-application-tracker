package server.middleware.security;

import java.util.Map;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import server.decorators.flow.api.Api;
import server.lib.combo.Kit;
import server.lib.data_structure.Prs;

@Component @Order(10) @RequiredArgsConstructor
public class CorsMdw implements WebFilter {

    private final Kit kit;

    @Override
    public Mono<Void> filter(ServerWebExchange exc, WebFilterChain chain) {
        var api = (Api) exc;
        var res = api.getResponse();

        String origin = api.getHeader(HttpHeaders.ORIGIN);
        String allowed = kit.getEnvKeeper().getFrontUrl();

        if (!origin.isBlank() && !origin.startsWith(allowed))
            return writeForbidden(res, origin);

        setCorsHeaders(res, allowed);

        if (HttpMethod.OPTIONS.equals(api.getMethod())) {
            res.setStatusCode(HttpStatus.OK);
            return res.setComplete();
        }

        return chain.filter(api);
    }

    private Mono<Void> writeForbidden(org.springframework.http.server.reactive.ServerHttpResponse res, String origin) {
        res.setStatusCode(HttpStatus.FORBIDDEN);
        res.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        String msg = String.format("❌ %s not allowed", origin);
        String body;
        try {
            body = kit.getJack().writeValueAsString(Map.of("msg", msg, "status", 403));
        } catch (JsonProcessingException err) {
            body = String.format("{ \"msg\": \"%s\", \"status\": 403 }", msg);
        }

        var buff = res.bufferFactory().wrap(Prs.utf8ToBinary(body));
        return res.writeWith(Mono.just(buff));
    }

    private void setCorsHeaders(org.springframework.http.server.reactive.ServerHttpResponse res, String allowed) {
        String allowedHdr = "Origin, Content-Type, Accept, Authorization";

        var resHdr = res.getHeaders();
        resHdr.set(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, allowedHdr);
        resHdr.set(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, allowed);
        resHdr.set(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "GET, POST, PUT, PATCH, DELETE, OPTIONS");
        resHdr.set(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
    }
}
