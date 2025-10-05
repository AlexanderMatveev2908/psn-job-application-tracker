package server.paperwork;

import java.util.Map;

import jakarta.validation.Valid;
import lombok.Data;
import server.paperwork.pair_pwd_annt.PairPwdMatch;

@Data @PairPwdMatch
public class PairPwdCheck {
  @Valid
  private final PwdCheck pwdCheck;

  @Valid
  private final ConfPwdCheck confPwdCheck;

  public PairPwdCheck(PwdCheck pwdCheck, ConfPwdCheck confPwdCheck) {
    this.pwdCheck = pwdCheck;
    this.confPwdCheck = confPwdCheck;
  }

  public PairPwdCheck(String pwd, String confPwd) {
    this.pwdCheck = new PwdCheck(pwd);
    this.confPwdCheck = new ConfPwdCheck(confPwd);
  }

  public String getPassword() {
    return pwdCheck.getPassword();
  }

  public String getConfirmPassword() {
    return confPwdCheck.getConfirmPassword();
  }

  public static PairPwdCheck fromBody(Map<String, Object> body) {
    return new PairPwdCheck(PwdCheck.fromBody(body), ConfPwdCheck.fromBody(body));
  }
}
