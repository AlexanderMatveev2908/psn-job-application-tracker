package server._lib_tests;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.RequestBodySpec;
import org.springframework.test.web.reactive.server.WebTestClient.RequestHeadersSpec;
import org.springframework.web.reactive.function.BodyInserters;

import server.lib.dev.MyLog;

public class ReqT {

    private final WebTestClient web;
    private final Map<String, String> headers = new HashMap<>();
    private final Map<String, String> cookies = new HashMap<>();
    private final Map<String, String> query = new HashMap<>();
    private String url;
    private String pathIdParam;
    private HttpMethod method = HttpMethod.GET;
    private Object body;
    private MultipartBodyBuilder multipartBody;

    private ReqT(WebTestClient web, String url) {
        this.web = web;
        this.url = url;
    }

    public static ReqT withUrl(WebTestClient web, String url) {
        return new ReqT(web, "/api/v1" + url);
    }

    public ReqT pathIdParam(UUID pathIdParam) {
        this.pathIdParam = pathIdParam.toString();
        return this;
    }

    public ReqT pathIdInvalid() {
        this.pathIdParam = "invalid_id";
        return this;
    }

    public ReqT method(HttpMethod method) {
        this.method = method;
        return this;
    }

    @SuppressWarnings({ "unused", "unchecked", "UseSpecificCatch", "CallToPrintStackTrace" })
    public ReqT body(Object body) {
        if (this.body == null) {
            this.body = body;
        } else {
            if (body instanceof Map argMap && this.body instanceof Map existingMap) {
                var newBody = new HashMap<>();
                newBody.putAll(existingMap);
                newBody.putAll(argMap);

                this.body = newBody;
            }
        }

        return this;
    }

    public ReqT addBdCbcHmac(String token) {
        return body(Map.of("cbcHmacToken", token));
    }

    public ReqT header(String key, String value) {
        this.headers.put(key, value);
        return this;
    }

    public ReqT addCk(String name, String value) {
        this.cookies.put(name, value);
        return this;
    }

    public ReqT jwt(String token) {
        return header("Authorization", "Bearer " + token);
    }

    public ReqT jwe(String token) {
        return addCk("refreshToken", token);
    }

    public ReqT addQuery(String key, String val) {
        this.query.put(key, val);
        return this;
    }

    public ReqT addQueryCbcHmac(String token) {
        this.addQuery("cbcHmacToken", token);
        return this;
    }

    public ReqT multipart(Map<String, Object> parts) {
        if (this.multipartBody == null) {
            this.multipartBody = new MultipartBodyBuilder();
        }
        parts.forEach((k, v) -> this.multipartBody.part(k, v));
        return this;
    }

    public ResT send() {
        String fullUrl = url;

        if (pathIdParam != null)
            fullUrl += ("/" + pathIdParam);

        if (!query.isEmpty()) {
            String joined = query.entrySet().stream().map(pair -> pair.getKey() + "=" + pair.getValue())
                    .collect(Collectors.joining("&"));
            fullUrl += "?" + joined;
        }

        RequestHeadersSpec<?> req = web.method(method).uri(fullUrl);

        if (!headers.isEmpty())
            req = req.headers(existing -> headers.forEach(existing::add));

        if (req instanceof RequestBodySpec bodySpec) {
            if (multipartBody != null) {
                req = bodySpec.contentType(MediaType.MULTIPART_FORM_DATA);

                req = bodySpec.body(BodyInserters.fromMultipartData(multipartBody.build()));
            } else if (body != null) {
                req = bodySpec.contentType(MediaType.APPLICATION_JSON);

                req = bodySpec.bodyValue(body);
            }
        }

        if (!cookies.isEmpty())
            for (var pair : cookies.entrySet())
                req = req.cookie(pair.getKey(), pair.getValue());

        var res = ResT.of(req.exchange().expectBody(new ParameterizedTypeReference<Map<String, Object>>() {
        }).returnResult());

        if (url.startsWith("/api/v1/test/"))
            return res;

        MyLog.startLog();
        System.out.println("🚦 " + url + " => " + res.getStatus());
        res.getBd().forEach((k, v) -> MyLog.logKV(k, v));
        MyLog.endLog();
        MyLog.wOk(res);

        return res;
    }

    public static ResT verifyMailWithToken(WebTestClient web, String token) {
        return ReqT.withUrl(web, "/verify/confirm-email").method(HttpMethod.GET).addQueryCbcHmac(token).send();
    }
}
