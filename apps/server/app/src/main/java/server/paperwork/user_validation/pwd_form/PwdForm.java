package server.paperwork.user_validation.pwd_form;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data @JsonIgnoreProperties(ignoreUnknown = true)
public class PwdForm implements PwdInt {
  private String password;
}
