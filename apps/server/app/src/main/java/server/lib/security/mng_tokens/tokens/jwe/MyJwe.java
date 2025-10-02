package server.lib.security.mng_tokens.tokens.jwe;

import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.Instant;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.JWEAlgorithm;
import com.nimbusds.jose.JWEHeader;
import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.RSADecrypter;
import com.nimbusds.jose.crypto.RSAEncrypter;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import server.conf.env_conf.EnvKeeper;
import server.decorators.flow.ErrAPI;
import server.lib.data_structure.Frmt;
import server.lib.security.hash.MyHashMng;
import server.lib.security.mng_tokens.etc.MyTkPayload;
import server.lib.security.mng_tokens.expiry_mng.ExpMng;
import server.lib.security.mng_tokens.expiry_mng.etc.RecExpTplSec;
import server.lib.security.mng_tokens.tokens.jwe.etc.RecCreateJweReturnT;
import server.models.token.MyToken;
import server.models.token.etc.AlgT;
import server.models.token.etc.TokenT;

@SuppressFBWarnings({ "EI2" }) @Service @RequiredArgsConstructor
public class MyJwe {

    private final EnvKeeper envKeeper;
    private final ExpMng expMng;
    private final MyHashMng hashMng;

    private String stripKey(String key, String type) {
        return key.replace(String.format("-----BEGIN %s KEY-----", type), "")
                .replace(String.format("-----END %s KEY-----", type), "").replaceAll("\\s", "");
    }

    private RSAPrivateKey getPrivateKey() throws Exception {
        String privKeyPem = Frmt.hexToUtf8(envKeeper.getJwePrivate());
        String base64Key = stripKey(privKeyPem, "PRIVATE");

        byte[] decoded = Base64.getDecoder().decode(base64Key);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return (RSAPrivateKey) keyFactory.generatePrivate(spec);
    }

    private RSAPublicKey getPublicKey() throws Exception {
        String pubKeyPem = Frmt.hexToUtf8(envKeeper.getJwePublic());
        pubKeyPem = stripKey(pubKeyPem, "PUBLIC");

        byte[] decoded = Base64.getDecoder().decode(pubKeyPem);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return (RSAPublicKey) keyFactory.generatePublic(spec);
    }

    public RecCreateJweReturnT create(UUID userId, boolean forceExp) {
        try {
            JWEHeader hdr = new JWEHeader.Builder(JWEAlgorithm.RSA_OAEP_256, EncryptionMethod.A256GCM).build();

            RecExpTplSec recExp = expMng.jwe();
            long exp = forceExp ? -recExp.exp() : recExp.exp();

            Map<String, Object> claims = Map.of("userId", userId, "iat", recExp.iat(), "exp", exp);

            JWEObject jwe = new JWEObject(hdr, new Payload(Frmt.toJson(claims)));

            RSAEncrypter encrypter = new RSAEncrypter(getPublicKey());
            jwe.encrypt(encrypter);

            String refreshToken = jwe.serialize();

            MyToken newToken = new MyToken(userId, AlgT.RSA_OAEP256_A256GCM, TokenT.REFRESH,
                    hashMng.hmacHash(refreshToken), exp);

            return new RecCreateJweReturnT(newToken, refreshToken);
        } catch (Exception err) {
            throw new ErrAPI("err creating jwe");
        }
    }

    public MyTkPayload check(String token) {
        try {
            JWEObject jwe = JWEObject.parse(token);

            RSADecrypter decrypter = new RSADecrypter(getPrivateKey());
            jwe.decrypt(decrypter);

            MyTkPayload payload = MyTkPayload.fromObj(jwe.getPayload().toJSONObject());

            if (payload.exp() < Instant.now().getEpochSecond())
                throw new ErrAPI("jwe_expired", 401, Map.of("argDeleteJwe", payload.userId()));

            return payload;
        } catch (Exception err) {
            var msg = err.getMessage();
            msg = msg.contains("jwe_expired") ? msg : "jwe_invalid";

            Map<String, Object> data = null;

            if (err instanceof ErrAPI errInst)
                data = errInst.getData();

            throw new ErrAPI(msg, 401, data);
        }
    }

}
