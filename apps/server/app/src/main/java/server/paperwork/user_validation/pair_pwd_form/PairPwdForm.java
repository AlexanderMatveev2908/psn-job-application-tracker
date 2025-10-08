package server.paperwork.user_validation.pair_pwd_form;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import server.paperwork.user_validation.ConfPwdInt;
import server.paperwork.user_validation.pair_pwd_form.annotations.PairPwdFormMatch;
import server.paperwork.user_validation.pwd_form.PwdInt;

@Data @PairPwdFormMatch @JsonIgnoreProperties(ignoreUnknown = true)
public class PairPwdForm implements PwdInt, ConfPwdInt {
  private String password;
  private String confirmPassword;
}