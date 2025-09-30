package server.lib.security.cbc_hmac;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import server.decorators.flow.ErrAPI;
import server.lib.data_structure.Frmt;
import server.lib.security.cbc_hmac.etc.RecAad;
import server.lib.security.cbc_hmac.etc.RecCbcHmacKeys;
import server.lib.security.expiry_mng.ExpMng;
import server.lib.security.hkdf.Hkdf;
import server.models.token.MyToken;
import server.models.token.etc.AlgT;
import server.models.token.etc.TokenT;

@Service
@RequiredArgsConstructor
public class CbcHmac {
    private final Hkdf hkdf;
    private final ExpMng expMng;

    public RecCbcHmacKeys deriveKeys(RecAad rec) {
        byte[] okm = hkdf.derive(rec, 64);

        byte[] aesKey = Arrays.copyOfRange(okm, 0, 32);
        byte[] hmacKey = Arrays.copyOfRange(okm, 32, 64);

        return new RecCbcHmacKeys(aesKey, hmacKey);
    }

    private String encode(byte[] arg) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(arg);
    }

    private IvParameterSpec genIv() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        return ivSpec;
    }

    private byte[] encrypt(byte[] plain, SecretKey aesKey, IvParameterSpec ivSpec) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, aesKey, ivSpec);
            byte[] ciphertext = cipher.doFinal(plain);

            return ciphertext;
        } catch (Exception err) {
            throw new ErrAPI("err aes encryption");
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
        } catch (Exception err) {
            throw new ErrAPI("err hmac hashing");
        }
    }

    public MyToken create(AlgT algT, TokenT tokenT, UUID userId) {
        RecAad aad = new RecAad(algT, tokenT, userId);

        RecCbcHmacKeys keys = deriveKeys(aad);

        SecretKey aesKey = new SecretKeySpec(keys.aesKey(), "AES");
        SecretKey hmacKey = new SecretKeySpec(keys.hmacKey(), "HmacSHA512");

        byte[] plain = Frmt.toJson(Map.of("userId", userId)).getBytes(StandardCharsets.UTF_8);

        IvParameterSpec ivSpec = genIv();

        byte[] ciphertext = encrypt(plain, aesKey, ivSpec);

        byte[] tag = hash(hmacKey, aad, ivSpec.getIV(), ciphertext);

        String clientToken = encode(ivSpec.getIV()) + "." + encode(ciphertext) + "." + encode(tag);

        return new MyToken(userId, tokenT, algT, clientToken, expMng.cbcHmac());
    }

}
