package server.lib.security.tfa.etc;

import java.util.Map;

import server.lib.data_structure.Prs;
import server.lib.security.tfa.bkp_codes.etc.RecBkpCodes;
import server.lib.security.tfa.totp.etc.RecTotpSecret;

public record Rec2FA(String zipB64, String qrB64, RecTotpSecret recTOTP, RecBkpCodes recBkpCodes) {

  public static Rec2FA parsing(byte[] zipBase64, byte[] qrBinary, RecTotpSecret recTOTP, RecBkpCodes recBkpCodes) {
    var parsedZip = "data:application/zip;base64," + Prs.binaryToBase64(zipBase64);
    var parsedQr = "data:image/png;base64," + Prs.binaryToBase64(qrBinary);

    return new Rec2FA(parsedZip, parsedQr, recTOTP, recBkpCodes);
  }

  public Map<String, Object> forClient() {
    return Map.of("totp_secret", recTOTP.uri(), "backup_codes", recBkpCodes().clientCodes(), "totp_secret_qrcode",
        qrB64(), "zip_file", zipB64());
  }
}
