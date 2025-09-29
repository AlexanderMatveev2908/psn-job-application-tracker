package server.lib.security.jwt;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;

import server.conf.env_conf.EnvKeeper;
import server.decorators.flow.ErrAPI;
import server.lib.data_structure.Frmt;

@Service
public final class MyJwt {
    private final EnvKeeper envKeeper;
    private final Algorithm alg;

    MyJwt(EnvKeeper envKeeper) {
        this.envKeeper = envKeeper;
        this.alg = Algorithm.HMAC256(envKeeper.getJwtSecret());
    }

    public String create(UUID userId) {

        Instant now = Instant.now();
        Date issuedAt = Date.from(now);
        Date expiresAt = Date.from(now.plus(15, ChronoUnit.MINUTES));

        return JWT.create()
                .withIssuer(envKeeper.getAppName())
                .withSubject(Frmt.toJson(Map.of("userId", userId)))
                .withIssuedAt(issuedAt)
                .withExpiresAt(expiresAt)
                .sign(alg);
    }

    public DecodedJWT check(String token) {

        try {
            JWTVerifier verifier = JWT.require(alg)
                    .withIssuer(envKeeper.getAppName())
                    .build();

            return verifier.verify(token);

        } catch (TokenExpiredException ex) {
            throw new ErrAPI("jwt_expired", 401);
        } catch (JWTVerificationException ex) {
            throw new ErrAPI("jwt_invalid", 401);
        } catch (Exception err) {
            throw new ErrAPI("err checking jwt");
        }
    }
}
