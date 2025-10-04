package server._lib_tests;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.RequestBodySpec;
import org.springframework.test.web.reactive.server.WebTestClient.RequestHeadersSpec;

import server.lib.dev.MyLog;

public class ReqT {

    private final WebTestClient web;
    private final Map<String, String> headers = new HashMap<>();
    private final Map<String, String> cookies = new HashMap<>();
    private final Map<String, String> query = new HashMap<>();
    private String url;
    private HttpMethod method = HttpMethod.GET;
    private Object body;

    private ReqT(WebTestClient web, String url) {
        this.web = web;
        this.url = url;
    }

    public static ReqT withUrl(WebTestClient web, String url) {
        return new ReqT(web, "/api/v1" + url);
    }

    public ReqT method(HttpMethod method) {
        this.method = method;
        return this;
    }

    public ReqT body(Object body) {
        this.body = body;
        return this;
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

    public ResT send() {
        String fullUrl = url;

        if (!query.isEmpty()) {
            String joined = query.entrySet().stream().map(pair -> pair.getKey() + "=" + pair.getValue())
                    .collect(Collectors.joining("&"));
            fullUrl += "?" + joined;
        }

        RequestHeadersSpec<?> req = web.method(method).uri(fullUrl);

        if (!headers.isEmpty())
            req = req.headers(existing -> headers.forEach(existing::add));

        if (body != null && req instanceof RequestBodySpec bodySpec)
            req = bodySpec.bodyValue(body);

        if (!cookies.isEmpty())
            for (var pair : cookies.entrySet())
                req = req.cookie(pair.getKey(), pair.getValue());

        var res = ResT.of(req.exchange().expectBody(new ParameterizedTypeReference<Map<String, Object>>() {
        }).returnResult());

        if (url.startsWith("/api/v1/test/"))
            return res;

        System.out.println("🚦 " + url + " => " + res.getStatus());
        System.out.println("📜 " + res.getHdrs());
        System.out.println("🍪 " + res.getCks());
        res.getBd().forEach((k, v) -> MyLog.logKV(k, v));
        MyLog.endLog();
        MyLog.wOk(res);

        return res;
    }

}
