package server._lib_tests;

import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.RequestBodySpec;
import org.springframework.test.web.reactive.server.WebTestClient.RequestHeadersSpec;

import lombok.RequiredArgsConstructor;
import server.lib.dev.MyLog;

@RequiredArgsConstructor
public final class ReqT {
    private final WebTestClient web;

    public ResT reqFull(String url, HttpMethod method, Object body, Map<String, String> hdr) {
        RequestHeadersSpec<?> req = web.method(method).uri(url);

        if (hdr != null && !hdr.isEmpty())
            req = req.headers(h -> hdr.forEach(h::add));

        if (body != null && req instanceof RequestBodySpec bodySpec)
            req = bodySpec.bodyValue(body);

        var res = ResT.of(
                req.exchange()
                        .expectBody(new ParameterizedTypeReference<Map<String, Object>>() {
                        })
                        .returnResult());

        MyLog.logTtl(url, "🚦 " + res.getStatus(), "📜 " + res.getHdrs(), "🍪 " + res.getCks());
        res.getBd().entrySet().stream()
                .forEach(pair -> MyLog.logKV(pair.getKey(), pair.getValue()));

        MyLog.wOk(res);

        return res;
    }

    public ResT reqHdr(String url, HttpMethod method, Map<String, String> hdr) {
        return reqFull(url, method, null, hdr);
    }

    public ResT reqBd(String url, HttpMethod method, Object body) {
        return reqFull(url, method, body, null);
    }

}
