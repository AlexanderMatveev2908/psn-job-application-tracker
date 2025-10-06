package server.lib.security.tfa.gcm;

import java.security.SecureRandom;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import server.conf.env_conf.EnvKeeper;
import server.decorators.flow.ErrAPI;
import server.lib.data_structure.Prs;

@Service @RequiredArgsConstructor
public class MyGCM {
  private static final String ALG = "AES/GCM/NoPadding";
  private static final int GCM_TAG_LEN = 128;
  private static final int IV_LEN = 12;
  private final EnvKeeper envKeeper;

  private SecretKey getKey() {
    return new SecretKeySpec(Prs.hexToBinary(envKeeper.getGcmKey()), "AES");
  }

  public String encrypt(String plainText) {
    try {
      byte[] iv = new byte[IV_LEN];
      new SecureRandom().nextBytes(iv);

      Cipher cipher = Cipher.getInstance(ALG);
      GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LEN, iv);
      cipher.init(Cipher.ENCRYPT_MODE, getKey(), spec);

      byte[] cipherText = cipher.doFinal(plainText.getBytes());
      byte[] combined = new byte[iv.length + cipherText.length];

      System.arraycopy(iv, 0, combined, 0, iv.length);
      System.arraycopy(cipherText, 0, combined, iv.length, cipherText.length);

      return Prs.binaryToHex(combined);
    } catch (Exception err) {
      throw new ErrAPI("err encrypting gcm");
    }
  }

  public String decrypt(String encrypted) throws Exception {
    byte[] decoded = Prs.hexToBinary(encrypted);

    byte[] iv = Arrays.copyOfRange(decoded, 0, IV_LEN);
    byte[] cipherText = Arrays.copyOfRange(decoded, IV_LEN, decoded.length);

    Cipher cipher = Cipher.getInstance(ALG);
    GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LEN, iv);
    cipher.init(Cipher.DECRYPT_MODE, getKey(), spec);

    byte[] plain = cipher.doFinal(cipherText);

    return Prs.binaryToUtf8(plain);
  }

}
