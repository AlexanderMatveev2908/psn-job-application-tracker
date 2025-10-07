package server.lib.security.tfa;

import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.springframework.stereotype.Service;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import server.conf.env_conf.EnvKeeper;
import server.conf.env_conf.etc.EnvMode;
import server.decorators.flow.ErrAPI;
import server.lib.data_structure.Prs;
import server.lib.paths.Hiker;
import server.lib.security.tfa.bkp_codes.GenBkpCodes;
import server.lib.security.tfa.bkp_codes.etc.RecBkpCodes;
import server.lib.security.tfa.etc.Rec2FA;
import server.lib.security.tfa.qr_code.MyQrCode;
import server.lib.security.tfa.totp.MyTotp;
import server.lib.security.tfa.totp.etc.RecTotpSecret;
import server.models.user.User;

@SuppressFBWarnings({ "EI2", "REC_CATCH_EXCEPTION" }) @Service @RequiredArgsConstructor
public class My2FA {
  private final GenBkpCodes bkpCodesMng;
  private final MyQrCode qqCodeMng;
  private final MyTotp totpMng;
  private final EnvKeeper envKeeper;

  public Mono<Rec2FA> setup2FA(User user) {
    var userEmail = user.getEmail();
    RecTotpSecret recTOTP = genTotpSecret(userEmail);

    return genBkpCodes().flatMap(recBkp -> genQrBinary(recTOTP.uri())
        .flatMap(qrBinary -> genZipBase64(recTOTP.uri(), recBkp.clientCodes(), qrBinary).map(zipBinary -> {
          saveLocally(user, zipBinary);

          return Rec2FA.parsing(zipBinary, qrBinary, recTOTP, recBkp);
        })));
  }

  public boolean checkTotp(String encryptedSecret, Integer totpCode) {
    return totpMng.checkTotp(encryptedSecret, totpCode);
  }

  private Mono<RecBkpCodes> genBkpCodes() {
    return bkpCodesMng.getCodes();
  }

  private Mono<byte[]> genQrBinary(String data) {
    return qqCodeMng.genQrBinary(data);
  }

  private RecTotpSecret genTotpSecret(String userEmail) {
    return totpMng.genSecret(userEmail);
  }

  private Mono<byte[]> genZipBase64(String totpUri, List<String> clientCodes, byte[] qrBinary) {
    return Mono.fromCallable(() -> {
      try (ByteArrayOutputStream bos = new ByteArrayOutputStream(); ZipOutputStream zos = new ZipOutputStream(bos)) {

        zos.putNextEntry(new ZipEntry("totp_secret.txt"));
        zos.write(Prs.utf8ToBinary(totpUri));
        zos.closeEntry();

        zos.putNextEntry(new ZipEntry("backup_codes.txt"));
        zos.write(Prs.utf8ToBinary(formatCodes(clientCodes)));
        zos.closeEntry();

        zos.putNextEntry(new ZipEntry("qrcode.png"));
        zos.write(qrBinary);
        zos.closeEntry();

        zos.finish();

        return bos.toByteArray();
      } catch (Exception err) {
        throw new ErrAPI("err generating zip");
      }
    }).subscribeOn(Schedulers.boundedElastic());
  }

  private String formatCodes(List<String> clientCodes) {

    StringBuilder sb = new StringBuilder();

    for (int i = 0; i < clientCodes.size(); i++) {
      var curr = clientCodes.get(i);
      sb.append(curr);
      sb.append(i % 2 != 0 ? "\n" : " ".repeat(4));
    }

    return sb.toString();
  }

  // ! DEV ONLY
  private void saveLocally(User user, byte[] binaryZip) {

    if (envKeeper.getEnvMode().equals(EnvMode.PROD))
      return;

    var mailUser = user.getEmail();

    if (!mailUser.contains("matveev"))
      return;

    Path filePath = Hiker.ASSETS_DIR.resolve(mailUser + ".zip").normalize();

    try {

      Files.write(filePath, binaryZip, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

    } catch (Exception err) {
      throw new ErrAPI("err saving local zip");
    }
  }

}
