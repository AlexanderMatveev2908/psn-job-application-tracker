package server.decorators.flow.api;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.ServerWebExchangeDecorator;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import server.decorators.flow.ErrAPI;
import server.decorators.flow.api.etc.ApiAttr;
import server.decorators.flow.api.etc.ApiInfo;

@SuppressWarnings({ "unused", "UseSpecificCatch" })
public final class Api extends ServerWebExchangeDecorator implements ApiInfo, ApiAttr {

    private static final ObjectMapper JACKSON = new ObjectMapper();
    private static final DefaultDataBufferFactory BUFFER_FACTORY = new DefaultDataBufferFactory();

    private final Mono<byte[]> cachedBody;

    public Api(ServerWebExchange exc) {
        super(exc);

        this.cachedBody = DataBufferUtils.join(exc.getRequest().getBody()).map(buf -> {
            byte[] bytes = new byte[buf.readableByteCount()];
            buf.read(bytes);
            DataBufferUtils.release(buf);
            return bytes;
        }).defaultIfEmpty(new byte[0]).cache();
    }

    @Override
    public ServerWebExchange getExch() {
        return this;
    }

    @Override
    public ServerHttpRequest getRequest() {
        return new ServerHttpRequestDecorator(super.getRequest()) {
            @Override
            public Flux<DataBuffer> getBody() {
                return cachedBody.flatMapMany(bytes -> {
                    if (bytes.length == 0)
                        return Flux.empty();
                    return Flux.just(BUFFER_FACTORY.wrap(bytes.clone()));
                });
            }
        };
    }

    public boolean isSamePath(String arg) {
        if (arg == null)
            return false;

        return getPath().equals(arg.split("\\?", 2)[0]);
    }

    public String getQueryCbcHmac() {
        if (getParsedQuery().orElse(Map.of()).get("cbcHmacToken") instanceof String cbcHmac)
            return cbcHmac;

        return "";
    }

    public Mono<String> getBdCbcHmac() {
        return getBd(new TypeReference<Map<String, Object>>() {
        }).flatMap(body -> {
            if (body.get("cbcHmacToken") instanceof String tokenStr && !tokenStr.isBlank())
                return Mono.just(tokenStr);
            return Mono.empty();
        });
    }

    public <T> Mono<T> getBd(TypeReference<T> type) {
        return cachedBody.flatMap(bytes -> {
            if (bytes.length == 0)
                return Mono.empty();

            return Mono.fromCallable(() -> JACKSON.readValue(bytes, type)).cache();
        }).onErrorMap(err -> new ErrAPI("wrong data format", 400));
    }

    public Mono<String> getBdStr() {
        return cachedBody.flatMap(optBytes -> {
            if (optBytes.length == 0)
                return Mono.empty();

            Charset charset = Optional.ofNullable(getRequest().getHeaders().getContentType()).map(MediaType::getCharset)
                    .orElse(StandardCharsets.UTF_8);

            return Mono.just(new String(optBytes, charset));
        }).cache();
    }

    public Mono<byte[]> getRawBd() {
        return cachedBody.map(bytes -> bytes.clone()).cache();
    }

    public boolean isResCmt() {
        return getResponse().isCommitted();
    }
}
