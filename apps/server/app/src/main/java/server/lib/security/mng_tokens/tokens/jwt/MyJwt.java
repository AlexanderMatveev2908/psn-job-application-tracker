package server.lib.security.mng_tokens.tokens.jwt;

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
import server.lib.security.mng_tokens.etc.MyTkPayload;
import server.lib.security.mng_tokens.expiry_mng.ExpMng;
import server.lib.security.mng_tokens.expiry_mng.etc.RecExpTplSec;

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

        RecExpTplSec rec = expMng.jwt();

        return JWT.create().withIssuer(envKeeper.getAppName()).withSubject(MyTkPayload.toString(userId, rec))
                .withIssuedAt(rec.toDate(rec.iat())).withExpiresAt(rec.toDate(rec.exp())).sign(alg);
    }

    public MyTkPayload check(String token) {

        try {
            JWTVerifier verifier = JWT.require(alg).withIssuer(envKeeper.getAppName()).build();

            DecodedJWT resCheck = verifier.verify(token);

            return MyTkPayload.fromString(resCheck.getSubject());

        } catch (TokenExpiredException err) {
            throw new ErrAPI("jwt_expired", 401);
        } catch (JWTVerificationException ex) {
            throw new ErrAPI("jwt_invalid", 401);
        } catch (Exception err) {
            throw new ErrAPI("err checking jwt");
        }
    }
}
