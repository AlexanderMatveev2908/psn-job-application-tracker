package server.lib.security.tfa.etc;

import java.util.List;
import java.util.Map;

import server.lib.data_structure.Prs;

public record Rec2FA(String zipB64, String qrB64, String totpSecretUri, List<String> bkpCodes) {

  public static Rec2FA parsing(byte[] zipBase64, byte[] qrBinary, String totpSecretUri, List<String> bkpCodes) {
    var parsedZip = "data:application/zip;base64," + Prs.binaryToBase64(zipBase64);
    var parsedQr = "data:image/png;base64," + Prs.binaryToBase64(qrBinary);

    return new Rec2FA(parsedZip, parsedQr, totpSecretUri, bkpCodes);
  }

  public Map<String, Object> toMap() {
    return Map.of("totp_secret", totpSecretUri(), "backup_codes", bkpCodes(), "totp_secret_qrcode", qrB64(), "zip_file",
        zipB64());
  }
}
