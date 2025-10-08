package server.paperwork.user_validation.email_form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public interface EmailSpec {

  @NotBlank(message = "email required") @Email(message = "email invalid") @Size(max = 254, message = "email can at most have 254 chars")
  String getEmail();

}