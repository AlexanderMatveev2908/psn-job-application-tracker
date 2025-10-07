package server.paperwork.tfa;

import java.util.Map;

import jakarta.validation.constraints.Pattern;
import lombok.Data;
import server.conf.Reg;
import server.decorators.flow.ErrAPI;
import server.paperwork.tfa.annotations.TFAFormMatch;

@Data @TFAFormMatch({ "totpCode", "backupCode" })
public class TFAForm {
  @Pattern(regexp = Reg.TOTP_CODE, message = "totp_code_invalid")
  private final String totpCode;

  @Pattern(regexp = Reg.BKP_CODE, message = "backup_code_invalid")
  private final String backupCode;

  public static TFAForm fromMap(Map<String, Object> map) {

    try {
      var rawTotp = map.get("totpCode");
      var totpCode = rawTotp instanceof Integer intCode ? intCode.toString()
          : rawTotp instanceof String strCode ? strCode : null;

      return new TFAForm(totpCode, (String) map.get("backupCode") instanceof String strCode ? strCode : null);
    } catch (Exception err) {
      throw new ErrAPI("invalid 2FA data", 401);
    }
  }

  public Integer getTotpInt() {
    return Integer.parseInt(totpCode);
  }
}
