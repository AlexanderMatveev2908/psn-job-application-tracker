package server.lib.security.cbc_hmac.etc;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

@SuppressFBWarnings({ "EI" })
public record RecCbcHmacKeys(byte[] aesKey, byte[] hmacKey) {

    public SecretKey getAesSpec() {
        return new SecretKeySpec(aesKey, "AES");
    }

    public SecretKey getHmacSpec() {
        return new SecretKeySpec(aesKey, "HmacSHA512");
    }
}
