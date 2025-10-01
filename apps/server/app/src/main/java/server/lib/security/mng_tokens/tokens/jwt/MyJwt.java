package server.lib.security.mng_tokens.tokens.jwt;

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
import server.lib.security.mng_tokens.expiry_mng.ExpMng;
import server.lib.security.mng_tokens.expiry_mng.etc.RecExpTplDate;

@Service

public final class MyJwt {
    private final EnvKeeper envKeeper;
    private final Algorithm alg;
    private final ExpMng expMng;

    MyJwt(EnvKeeper envKeeper, ExpMng expMng) {
        this.envKeeper = envKeeper;
        this.alg = Algorithm.HMAC256(envKeeper.getJwtSecret());
        this.expMng = expMng;
    }

    public String create(UUID userId) {

        RecExpTplDate rec = expMng.jwt();

        return JWT.create()
                .withIssuer(envKeeper.getAppName())
                .withSubject(Frmt.toJson(Map.of("userId", userId)))
                .withIssuedAt(rec.iat())
                .withExpiresAt(rec.exp())
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
