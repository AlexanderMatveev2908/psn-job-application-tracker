package server.lib.security.cookies;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import server.conf.env_conf.EnvKeeper;
import server.conf.env_conf.etc.EnvMode;
import server.lib.security.mng_tokens.expiry_mng.ExpMng;

@Service @SuppressFBWarnings({ "EI2" })
public class MyCookies {
    private final boolean isTest;
    private final ExpMng expMng;

    MyCookies(EnvKeeper envKeeper, ExpMng expMng) {
        this.isTest = envKeeper.getEnvMode().equals(EnvMode.TEST);
        this.expMng = expMng;
    }

    public ResponseCookie jweCookie(String clientToken) {
        return ResponseCookie.from("refreshToken", clientToken).httpOnly(true).secure(!isTest).path("/")
                .maxAge(expMng.jwe().exp() + 60).sameSite(isTest ? "Lax" : "None").build();
    }

    public ResponseCookie delJweCookie() {
        return ResponseCookie.from("refreshToken", "").httpOnly(true).secure(!isTest).path("/").maxAge(0)
                .sameSite(isTest ? "Lax" : "None").build();
    }
}
