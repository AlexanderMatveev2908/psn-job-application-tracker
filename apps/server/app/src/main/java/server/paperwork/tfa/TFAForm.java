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
      return new TFAForm((String) map.get("totpCode"), (String) map.get("backupCode"));
    } catch (Exception err) {
      throw new ErrAPI("invalid 2FA data", 401);
    }
  }
}
