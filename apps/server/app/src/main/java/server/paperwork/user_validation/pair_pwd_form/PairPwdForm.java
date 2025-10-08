package server.paperwork.user_validation.pair_pwd_form;

import java.util.Map;

import jakarta.validation.Valid;
import lombok.Data;
import server.paperwork.user_validation.ConfPwdForm;
import server.paperwork.user_validation.PwdForm;
import server.paperwork.user_validation.pair_pwd_form.annotations.PairPwdFormMatch;

@Data @PairPwdFormMatch
public class PairPwdForm {
  @Valid
  private final PwdForm pwdCheck;

  @Valid
  private final ConfPwdForm confPwdCheck;

  public PairPwdForm(PwdForm pwdCheck, ConfPwdForm confPwdCheck) {
    this.pwdCheck = pwdCheck;
    this.confPwdCheck = confPwdCheck;
  }

  public PairPwdForm(String pwd, String confPwd) {
    this.pwdCheck = new PwdForm(pwd);
    this.confPwdCheck = new ConfPwdForm(confPwd);
  }

  public String getPassword() {
    return pwdCheck.getPassword();
  }

  public String getConfirmPassword() {
    return confPwdCheck.getConfirmPassword();
  }

  public static PairPwdForm fromBody(Map<String, Object> body) {
    return new PairPwdForm(PwdForm.fromBody(body), ConfPwdForm.fromBody(body));
  }
}
