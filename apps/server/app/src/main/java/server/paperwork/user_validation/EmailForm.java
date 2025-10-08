package server.paperwork.user_validation;

import java.util.Map;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import server.decorators.flow.ErrAPI;

@Data
public class EmailForm {
  @NotBlank(message = "email required") @Email(message = "email invalid") @Size(max = 254, message = "email can at most have 254 chars")
  private final String email;

  public static EmailForm fromBody(Map<String, Object> body) {
    if (body.get("email") instanceof String email)
      return new EmailForm(email);

    throw new ErrAPI("email invalid", 400);
  }
}
