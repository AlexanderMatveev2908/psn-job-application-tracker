package server._lib_tests;

import lombok.AllArgsConstructor;
import lombok.Data;
import server.decorators.RootCls;
import server.models.user.User;

import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.util.MultiValueMap;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

@Data @AllArgsConstructor @SuppressFBWarnings({ "EI" })
public class ResT implements RootCls {

    private final int status;
    private final HttpHeaders hdrs;
    private final MultiValueMap<String, ResponseCookie> cks;

    private final Map<String, Object> bd;

    public static ResT of(EntityExchangeResult<Map<String, Object>> res) {
        Map<String, Object> bd = res.getResponseBody();

        return new ResT(res.getStatus().value(), res.getResponseHeaders(), res.getResponseCookies(), bd);
    }

    public String getHdr(String k) {
        return hdrs.getFirst(k);
    }

    public String getCk(String k) {
        var cksByKey = cks.get(k);
        if (cksByKey != null && !cksByKey.isEmpty())
            return cksByKey.get(0).getValue();

        var setCookies = hdrs.get(HttpHeaders.SET_COOKIE);
        if (setCookies != null)
            for (String cookieStr : setCookies)
                if (cookieStr.startsWith(k + "="))
                    return cookieStr.split(";", 2)[0].substring(k.length() + 1);

        return null;
    }

    public String getJwt() {
        return (String) getBd().get("accessToken");
    }

    public String getJwe() {
        return getCk("refreshToken");
    }

    public String getCbcHmac() {
        return (String) getBd().get("cbcHmacToken");
    }

    public String getMsg() {
        return (String) getBd().get("msg");
    }

    public User getUser() {
        return (User) getBd().get("user");
    }
}
