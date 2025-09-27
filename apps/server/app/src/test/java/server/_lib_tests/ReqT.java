package server._lib_tests;

import java.util.HashMap;
import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.RequestBodySpec;
import org.springframework.test.web.reactive.server.WebTestClient.RequestHeadersSpec;

import server.lib.dev.MyLog;

public class ReqT {

    private final WebTestClient web;
    private final String url;
    private HttpMethod method = HttpMethod.GET;
    private Object body;
    private final Map<String, String> headers = new HashMap<>();

    private ReqT(WebTestClient web, String url) {
        this.web = web;
        this.url = url;
    }

    public static ReqT withUrl(WebTestClient web, String url) {
        return new ReqT(web, url);
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

    public ResT send() {
        RequestHeadersSpec<?> req = web.method(method).uri(url);

        if (!headers.isEmpty())
            req = req.headers(existing -> headers.forEach(existing::add));

        if (body != null && req instanceof RequestBodySpec bodySpec)
            req = bodySpec.bodyValue(body);

        var res = ResT.of(
                req.exchange()
                        .expectBody(new ParameterizedTypeReference<Map<String, Object>>() {
                        })
                        .returnResult());

        MyLog.logTtl(url, "🚦 " + res.getStatus(), "📜 " + res.getHdrs(), "🍪 " + res.getCks());
        res.getBd().forEach((k, v) -> MyLog.logKV(k, v));
        MyLog.wOk(res);

        return res;
    }

}
