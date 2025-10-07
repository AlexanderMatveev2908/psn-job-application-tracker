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

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import server.conf.env_conf.EnvKeeper;
import server.decorators.flow.ErrAPI;
import server.lib.security.tfa.gcm.MyGCM;
import server.lib.security.tfa.totp.etc.RecTotpSecret;

@SuppressFBWarnings({ "EI2", }) @Service @RequiredArgsConstructor
public class MyTotp {

  private static final SecureRandom random = new SecureRandom();
  private final MyGCM gcmMng;
  private final EnvKeeper envKeeper;

  public RecTotpSecret genSecret(String userEmail) {

    byte[] randomBytes = new byte[20];
    random.nextBytes(randomBytes);

    byte[] encoded = Base32.encode(randomBytes);
    String secret = new String(encoded, StandardCharsets.US_ASCII).replace("=", "");

    var issuer = envKeeper.getAppName();
    String uri = String.format("otpauth://totp/%s:%s?secret=%s&issuer=%s&algorithm=SHA1&digits=6&period=30", issuer,
        userEmail, secret, issuer);

    return new RecTotpSecret(secret, uri, gcmMng.encrypt(secret.getBytes(StandardCharsets.US_ASCII)));
  }

  public String plainB32FromEncHex(String hexEncrypt) {
    byte[] plainBinary = gcmMng.decrypt(hexEncrypt);

    return new String(plainBinary, StandardCharsets.US_ASCII);
  }

  public int genTestTOTP(String plainB32) {

    try {
      TimeBasedOneTimePasswordGenerator totp = new TimeBasedOneTimePasswordGenerator(Duration.ofSeconds(30));
      Instant now = Instant.now();
      byte[] keyBytes = Base32.decode(plainB32);
      SecretKey key = new SecretKeySpec(keyBytes, "HmacSHA1");

      return totp.generateOneTimePassword(key, now);

    } catch (Exception err) {
      throw new ErrAPI("err generating TOTP code");
    }
  }

  public boolean checkTotp(String encryptedHex, int code) {
    try {
      // ? when i grab secret from database as user prop
      String plainB32 = plainB32FromEncHex(encryptedHex);
      byte[] keyBytes = Base32.decode(plainB32);
      SecretKey key = new SecretKeySpec(keyBytes, "HmacSHA1");

      TimeBasedOneTimePasswordGenerator totp = new TimeBasedOneTimePasswordGenerator(Duration.ofSeconds(30));
      Instant now = Instant.now();

      for (int offset = -1; offset <= 1; offset++) {
        Instant moment = now.plusSeconds(offset * 30);
        int currCode = totp.generateOneTimePassword(key, moment);

        if (currCode == code)
          return true;
      }

      return false;
    } catch (Exception err) {
      throw new ErrAPI("totp_invalid", 401);
    }
  }

}
