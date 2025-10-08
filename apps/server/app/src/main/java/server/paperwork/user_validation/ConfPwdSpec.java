package server.paperwork.user_validation;

import jakarta.validation.constraints.NotBlank;

public interface ConfPwdSpec {
  @NotBlank(message = "confirm password required")
  String getConfirmPassword();
}