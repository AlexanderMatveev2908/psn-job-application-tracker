package server.paperwork;

import java.util.Map;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import server.conf.Reg;
import server.decorators.flow.ErrAPI;

@Data
public class PwdCheck {
  @NotBlank(message = "password required") @Pattern(regexp = Reg.PWD, message = "password invalid")
  private final String password;

  public static PwdCheck fromBody(Map<String, Object> body) {

    if (body.get("password") instanceof String pwdStr)
      return new PwdCheck(pwdStr);

    throw new ErrAPI("password not provided", 400);
  }
}
