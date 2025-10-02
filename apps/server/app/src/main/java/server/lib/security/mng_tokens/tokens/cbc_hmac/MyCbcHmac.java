package server.lib.security.mng_tokens.tokens.cbc_hmac;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import org.springframework.stereotype.Service;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import server.decorators.flow.ErrAPI;
import server.lib.data_structure.Frmt;
import server.lib.security.hash.MyHashMng;
import server.lib.security.hkdf.Hkdf;
import server.lib.security.mng_tokens.expiry_mng.ExpMng;
import server.lib.security.mng_tokens.expiry_mng.etc.RecExpTplSec;
import server.lib.security.mng_tokens.tokens.cbc_hmac.etc.RecAad;
import server.lib.security.mng_tokens.tokens.cbc_hmac.etc.RecCbcHmacKeys;
import server.lib.security.mng_tokens.tokens.cbc_hmac.etc.RecCreateCbcHmacReturnT;
import server.models.token.MyToken;
import server.models.token.etc.AlgT;
import server.models.token.etc.TokenT;

@Service @RequiredArgsConstructor @SuppressFBWarnings({ "REC" })
public class MyCbcHmac {
    private static final SecureRandom secureRandom = new SecureRandom();

    private final Hkdf hkdf;
    private final ExpMng expMng;
    private final MyHashMng hashMng;

    private RecCbcHmacKeys deriveKeys(RecAad rec) {
        byte[] okm = hkdf.derive(rec, 64);

        byte[] aesKey = Arrays.copyOfRange(okm, 0, 32);
        byte[] hmacKey = Arrays.copyOfRange(okm, 32, 64);

        return new RecCbcHmacKeys(aesKey, hmacKey);
    }

    private IvParameterSpec genIv() {
        byte[] iv = new byte[16];
        secureRandom.nextBytes(iv);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        return ivSpec;
    }

    private byte[] encrypt(SecretKey aesKey, IvParameterSpec ivSpec, byte[] plain) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, aesKey, ivSpec);
            byte[] ciphertext = cipher.doFinal(plain);

            return ciphertext;
        } catch (Exception err) {
            throw new ErrAPI("err encryption aes");
        }
    }

    private Map<String, Object> decrypt(SecretKey aesKey, byte[] iv, byte[] ciphertext) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, aesKey, new IvParameterSpec(iv));
            byte[] plain = cipher.doFinal(ciphertext);

            return Frmt.jsonToMap(new String(plain, StandardCharsets.UTF_8));
        } catch (Exception err) {
            throw new ErrAPI("cbc_hmac_invalid", 401);
        }
    }

    private byte[] hash(SecretKey hmacKey, RecAad aad, byte[] iv, byte[] ciphertext) {
        try {

            Mac mac = Mac.getInstance("HmacSHA512");
            mac.init(hmacKey);
            mac.update(aad.toBinary());
            mac.update(iv);
            mac.update(ciphertext);
            byte[] tag = mac.doFinal();

            return tag;
        } catch (NoSuchAlgorithmException | InvalidKeyException err) {
            throw new ErrAPI("err hmac hashing");
        }
    }

    public RecCreateCbcHmacReturnT create(TokenT tokenT, UUID userId) {
        RecAad aad = new RecAad(tokenT, userId);

        RecCbcHmacKeys keys = deriveKeys(aad);

        RecExpTplSec recExp = expMng.cbcHmac();
        byte[] plain = Frmt.mapToBinary(Map.of("userId", userId, "iat", recExp.iat(), "exp", recExp.exp()));

        IvParameterSpec ivSpec = genIv();

        byte[] ciphertext = encrypt(keys.getAesSpec(), ivSpec, plain);

        byte[] tag = hash(keys.getHmacSpec(), aad, ivSpec.getIV(), ciphertext);

        String clientToken = Frmt.binaryToHex(aad.toBinary()) + "." + Frmt.binaryToHex(ivSpec.getIV()) + "."
                + Frmt.binaryToHex(ciphertext) + "." + Frmt.binaryToHex(tag);

        var newToken = new MyToken(aad.getTokenId(), userId, aad.getAlgT(), tokenT, hashMng.hmacHash(clientToken),
                recExp.exp());

        return new RecCreateCbcHmacReturnT(newToken, clientToken);
    }

    public Map<String, Object> check(String clientToken, AlgT algT, TokenT tokenT, UUID userId) {

        String[] parts = clientToken.split("\\.");
        if (parts.length != 4)
            throw new ErrAPI("cbc_hmac_invalid", 401);

        RecAad aad = RecAad.fromPart(parts[0]);

        byte[] iv = Frmt.hexToBinary(parts[1]);
        byte[] cyphertext = Frmt.hexToBinary(parts[2]);
        byte[] tag = Frmt.hexToBinary(parts[3]);

        RecCbcHmacKeys keys = deriveKeys(aad);

        byte[] recomputed = hash(keys.getHmacSpec(), aad, iv, cyphertext);

        if (!Arrays.equals(tag, recomputed))
            throw new ErrAPI("cbc_hmac_invalid", 401);

        Map<String, Object> payload = decrypt(keys.getAesSpec(), iv, cyphertext);

        return payload;
    }

}
