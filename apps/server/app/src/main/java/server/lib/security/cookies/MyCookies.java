package server.lib.security.cookies;

import java.time.Duration;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import server.conf.env_conf.EnvKeeper;
import server.conf.env_conf.etc.EnvMode;

@Service @SuppressFBWarnings({ "EI2" })
public class MyCookies {
    private final boolean isTest;

    MyCookies(EnvKeeper envKeeper) {
        this.isTest = envKeeper.getEnvMode().equals(EnvMode.TEST);
    }

    public ResponseCookie jweCookie(String hashed) {
        return ResponseCookie.from("refreshToken", hashed).httpOnly(true).secure(!isTest).path("/")
                .maxAge(Duration.ofMinutes(15)).sameSite(isTest ? "Lax" : "None").build();
    }

    public ResponseCookie delJweCookie() {
        return ResponseCookie.from("refreshToken", "").httpOnly(true).secure(!isTest).path("/").maxAge(0)
                .sameSite(isTest ? "Lax" : "None").build();
    }
}
