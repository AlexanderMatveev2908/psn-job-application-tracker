package server.lib.security.MyCookies;

import java.time.Duration;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import server.conf.env_conf.EnvKeeper;
import server.conf.env_conf.etc.EnvMode;

@Service
@RequiredArgsConstructor
public class MyCookies {
    private final EnvKeeper envKeeper;

    public ResponseCookie genRefreshCookie(String hashed) {
        return ResponseCookie.from("refreshToken", hashed)
                .httpOnly(true)
                .secure(!envKeeper.getEnvMode().equals(EnvMode.TEST))
                .path("/")
                .maxAge(Duration.ofMinutes(15))
                .build();
    }
}
