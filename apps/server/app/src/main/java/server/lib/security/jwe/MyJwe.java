package server.lib.security.jwe;

import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
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
import server.lib.security.expiry_mng.ExpMng;
import server.lib.security.expiry_mng.etc.RecExpJwe;
import server.lib.security.jwe.etc.RecResJwe;

@SuppressFBWarnings({ "EI2" })
@Service
@RequiredArgsConstructor
public class MyJwe {

    private final EnvKeeper envKeeper;
    private final ExpMng expMng;

    private String stripKey(String key, String type) {
        return key.replace(String.format("-----BEGIN %s KEY-----", type), "")
                .replace(String.format("-----END %s KEY-----", type), "")
                .replaceAll("\\s", "");
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

    public RecResJwe create(UUID userId) {
        try {
            JWEHeader hdr = new JWEHeader.Builder(JWEAlgorithm.RSA_OAEP_256,
                    EncryptionMethod.A256GCM)
                    .build();

            RecExpJwe rec = expMng.jwe();

            Map<String, Object> claims = Map.of(
                    "userId", userId, "iat", rec.now(),
                    "exp", rec.exp());

            JWEObject jwe = new JWEObject(hdr, new Payload(Frmt.toJson(claims)));

            RSAEncrypter encrypter = new RSAEncrypter(getPublicKey());
            jwe.encrypt(encrypter);

            String refreshToken = jwe.serialize();

            return new RecResJwe(refreshToken, rec.exp());
        } catch (Exception err) {
            throw new ErrAPI("err creating jwe");
        }
    }

    public Map<String, Object> check(String token) {
        try {
            JWEObject jwe = JWEObject.parse(token);

            RSADecrypter decrypter = new RSADecrypter(getPrivateKey());
            jwe.decrypt(decrypter);

            Map<String, Object> payload = jwe.getPayload().toJSONObject();

            if ((long) payload.get("exp") < System.currentTimeMillis() / 1000L)
                throw new ErrAPI("jwe_expired", 401);

            return payload;
        } catch (Exception err) {
            var msg = err.getMessage();
            msg = msg.contains("jwe_expired") ? msg.substring(1) : "jwe_invalid";

            throw new ErrAPI(msg, 401);
        }
    }

}
