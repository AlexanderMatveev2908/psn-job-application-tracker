package server.paperwork;

import java.util.Map;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import server.decorators.flow.ErrAPI;

@Data
public class ConfPwdForm {
  @NotBlank(message = "confirm password required")
  private final String confirmPassword;

  public static ConfPwdForm fromBody(Map<String, Object> body) {
    if (body.get("confirmPassword") instanceof String pwdStr)
      return new ConfPwdForm(pwdStr);

    throw new ErrAPI("confirm password invalid", 400);
  }
}
