package server.lib.security.tfa.totp;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.util.encoders.Base32;
import org.springframework.stereotype.Service;

import com.eatthepath.otp.TimeBasedOneTimePasswordGenerator;

import lombok.RequiredArgsConstructor;
import server.conf.env_conf.EnvKeeper;
import server.decorators.flow.ErrAPI;
import server.lib.security.tfa.gcm.MyGCM;
import server.lib.security.tfa.totp.etc.RecTotpSecret;

@Service @RequiredArgsConstructor
public class MyTotp {

  private final MyGCM gcmMng;
  private final EnvKeeper envKeeper;

  public RecTotpSecret genSecret(String userEmail) {

    byte[] randomBytes = new byte[20];
    new SecureRandom().nextBytes(randomBytes);

    byte[] encoded = Base32.encode(randomBytes);
    String secret = new String(encoded, StandardCharsets.US_ASCII).replace("=", "");

    var issuer = envKeeper.getAppName();
    String uri = String.format("otpauth://totp/%s:%s?secret=%s&issuer=%s&algorithm=SHA1&digits=6&period=30", issuer,
        userEmail, secret, issuer);

    return new RecTotpSecret(secret, uri, gcmMng.encrypt(secret));
  }

  public int genCode(String secret) throws Exception {
    byte[] keyBytes = Base32.decode(secret);

    SecretKey key = new SecretKeySpec(keyBytes, "HmacSHA1");

    TimeBasedOneTimePasswordGenerator totp = new TimeBasedOneTimePasswordGenerator(Duration.ofSeconds(30));

    Instant now = Instant.now();
    int code = totp.generateOneTimePassword(key, now);

    return code;
  }

  public boolean checkTotp(String secret, int code) {

    try {
      return genCode(secret) == code;
    } catch (Exception err) {
      throw new ErrAPI("totp_invalid", 401);
    }
  }

}
