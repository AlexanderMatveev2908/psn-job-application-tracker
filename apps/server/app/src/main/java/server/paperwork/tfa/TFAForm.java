package server.paperwork.tfa;

import java.util.Map;
import java.util.Optional;

import jakarta.validation.constraints.Pattern;
import lombok.Data;
import server.conf.Reg;
import server.decorators.flow.ErrAPI;
import server.lib.data_structure.ShapeCheck;
import server.lib.dev.MyLog;
import server.paperwork.tfa.annotations.TFAFormMatch;

@Data @TFAFormMatch({ "totpCode", "backupCode" })
public class TFAForm {
  @Pattern(regexp = Reg.TOTP_CODE, message = "totp_code_invalid")
  private final String totpCode;

  @Pattern(regexp = Reg.BKP_CODE, message = "backup_code_invalid")
  private final String backupCode;

  public static TFAForm fromMap(Map<String, Object> map) {

    try {
      Object rawTotp = map.get("totpCode");
      String totpCode = null;

      if (rawTotp != null)
        totpCode = rawTotp instanceof Number num ? String.format("%06d", num.intValue()) : (String) rawTotp;

      Object rawBkp = map.get("backupCode");
      String bkpCode = ShapeCheck.isStr(rawBkp) ? (String) rawBkp : null;

      MyLog.log(totpCode, bkpCode);

      return new TFAForm(totpCode, bkpCode);
    } catch (Exception err) {
      throw new ErrAPI("invalid 2FA data", 401);
    }
  }

  public Optional<Integer> getTotpInt() {

    if (ShapeCheck.isStr(totpCode))
      return Optional.of(Integer.parseInt(totpCode));

    return Optional.empty();
  }
}
