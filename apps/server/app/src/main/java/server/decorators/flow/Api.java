package server.decorators.flow;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.ServerWebExchangeDecorator;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import server.lib.data_structure.ShapeCheck;
import server.models.user.User;

@SuppressWarnings({ "unused", "unchecked", "UseSpecificCatch" })
public final class Api extends ServerWebExchangeDecorator {

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

    public String getPath() {
        return getRequest().getPath().toString();
    }

    public boolean isSamePath(String arg) {
        if (arg == null)
            return false;

        return getPath().equals(arg.split("\\?", 2)[0]);
    }

    public HttpMethod getMethod() {
        return getRequest().getMethod();
    }

    public String getContentType() {
        return Optional.ofNullable(getRequest().getHeaders().getContentType()).map(MediaType::toString).orElse("");
    }

    public String getHeader(String name) {
        return Optional.ofNullable(getRequest().getHeaders().getFirst(name)).orElse("");
    }

    public void addHeader(String k, Object v) {
        getResponse().getHeaders().add(k, String.valueOf(v));
    }

    public String getIp() {
        var req = getRequest();

        return Optional.ofNullable(req.getRemoteAddress()).map(addr -> addr.getAddress())
                .map(inet -> inet.getHostAddress()).orElse("unknown");
    }

    public String getQuery() {
        return Optional.ofNullable(getRequest().getURI().getQuery()).orElse("");
    }

    public String getCookie(String name) {
        return Optional.ofNullable(getRequest().getCookies().getFirst(name)).map(cookie -> cookie.getValue())
                .orElse("");
    }

    public String getJwt() {
        String auth = getHeader("authorization");
        String token = auth.startsWith("Bearer ") ? auth.substring("Bearer ".length()) : "";

        return token;
    }

    public String getJwe() {
        String jwe = getCookie("refreshToken");

        return jwe;
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

    public User getUser() {
        return getAttribute("user");
    }

    public Optional<Map<String, Object>> getParsedForm() {
        return Optional.ofNullable((Map<String, Object>) this.getAttribute("parsedForm"));
    }

    public Optional<Map<String, Object>> getParsedQuery() {
        return Optional.ofNullable((Map<String, Object>) this.getAttribute("parsedQuery"));
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

    public <T> void setAttr(String key, T value) {
        if (key == null)
            return;
        if (value == null)
            getAttributes().remove(key);
        else
            getAttributes().put(key, value);

    }

    public <T> void setMappedDataAttr(T data) {
        setAttr("mappedData", data);
    }

    public void setParsedQueryAttr(Map<String, Object> parsed) {
        setAttr("parsedQuery", parsed);
    }

    public void setParsedFormAttr(Map<String, Object> parsed) {
        setAttr("parsedForm", parsed);
    }

    public void setUserAttr(User user) {
        setAttr("user", user);
    }

    // ? the middleware FormChecker after parsed the body to Map<String,Object>
    // ? has checked with Hibernate validator that the body respect shape
    // ? expected defined in a separate class.
    // ? at this point mappedData could be RegisterForm, LoginForm etc...
    public <T> T getMappedData() {
        return getAttribute("mappedData");
    }

    public boolean isResCmt() {
        return getResponse().isCommitted();
    }
}
