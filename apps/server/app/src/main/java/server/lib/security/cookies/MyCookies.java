package server.lib.security.cookies;

import java.time.Duration;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import server.conf.env_conf.EnvKeeper;
import server.conf.env_conf.etc.EnvMode;

@Service
@RequiredArgsConstructor
@SuppressFBWarnings({ "EI2" })
public class MyCookies {
    private final EnvKeeper envKeeper;

    public ResponseCookie genRefreshCookie(String hashed) {
        boolean isTest = envKeeper.getEnvMode().equals(EnvMode.TEST);

        return ResponseCookie.from("refreshToken", hashed)
                .httpOnly(true)
                .secure(!isTest)
                .path("/")
                .maxAge(Duration.ofMinutes(15))
                .sameSite(isTest ? "Lax" : "None")
                .build();
    }
}
