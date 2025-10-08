package server.paperwork.user_validation.pwd_form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import server.conf.Reg;

public interface PwdInt {
  @NotBlank(message = "password required") @Pattern(regexp = Reg.PWD, message = "password invalid") @Size(max = 100, message = "password must be within 100 chars")
  String getPassword();
}