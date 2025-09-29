package server.lib.jwt;

import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import lombok.RequiredArgsConstructor;
import server.conf.env_conf.EnvKeeper;

@Service
@RequiredArgsConstructor
public final class MyJwt {
    private final EnvKeeper envKeeper;
    private static final String SECRET = "super_super_secret_key_123";

    public String create(String userId) {
        Algorithm alg = Algorithm.HMAC256(SECRET);

        return JWT.create()
                .withIssuer(envKeeper.getAppName())
                .withSubject(userId)
                .withClaim("role", "admin")
                .withIssuedAt(new java.util.Date())
                .withExpiresAt(new java.util.Date(System.currentTimeMillis() + 3600_000))
                .sign(alg);
    }

    public DecodedJWT check(String token) {
        Algorithm alg = Algorithm.HMAC256(SECRET);

        JWTVerifier verifier = JWT.require(alg)
                .withIssuer(envKeeper.getAppName())
                .build();

        return verifier.verify(token);
    }
}
