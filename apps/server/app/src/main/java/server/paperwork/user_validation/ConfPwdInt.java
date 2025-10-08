package server.paperwork.user_validation;

import jakarta.validation.constraints.NotBlank;

public interface ConfPwdInt {
  @NotBlank(message = "confirm password required")
  String getConfirmPassword();
}