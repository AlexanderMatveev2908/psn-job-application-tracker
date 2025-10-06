package server.lib.security.tfa;

import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import server.conf.env_conf.EnvKeeper;
import server.conf.env_conf.etc.EnvMode;
import server.decorators.flow.Api;
import server.decorators.flow.ErrAPI;
import server.lib.data_structure.Prs;
import server.lib.dev.MyLog;
import server.lib.paths.Hiker;
import server.lib.security.tfa.bkp_codes.GenBkpCodes;
import server.lib.security.tfa.bkp_codes.etc.RecBkpCodes;
import server.lib.security.tfa.etc.Rec2FA;
import server.lib.security.tfa.qr_code.MyQrCode;
import server.lib.security.tfa.totp.MyTotp;
import server.lib.security.tfa.totp.etc.RecTotpSecret;

@Service @RequiredArgsConstructor
public class My2FA {
  private final GenBkpCodes bkpCodesMng;
  private final MyQrCode qqCodeMng;
  private final MyTotp totpMng;
  private final EnvKeeper envKeeper;

  public Mono<Rec2FA> setup2FA(Api api) {
    var userEmail = api.getUser().getEmail();
    RecTotpSecret recTOTP = genTotpSecret(userEmail);

    return genBkpCodes().flatMap(recBkp -> genQrBinary(userEmail)
        .flatMap(qrBinary -> genZipBase64(recTOTP.uri(), recBkp.clientCodes(), qrBinary).map(zipBinary -> {
          saveLocally(api, zipBinary);

          return Rec2FA.parsing(zipBinary, qrBinary, recTOTP.uri(), recBkp.clientCodes());
        })));
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
  private void saveLocally(Api api, byte[] binaryZip) {

    if (envKeeper.getEnvMode().equals(EnvMode.PROD))
      return;

    var mailUser = api.getUser().getEmail();
    Path filePath = Hiker.ASSETS_DIR.resolve(mailUser + ".zip").normalize();

    try {

      Files.write(filePath, binaryZip, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

      MyLog.log("🗃️ saved ZIP => " + filePath.toString());
    } catch (Exception err) {
      throw new ErrAPI("err saving local zip");
    }
  }

}
