package server.paperwork;

import java.util.Map;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import server.decorators.flow.ErrAPI;

@Data
public class EmailForm {
  @NotBlank(message = "email required") @Email(message = "email invalid")
  private final String email;

  public static EmailForm fromBody(Map<String, Object> body) {
    if (body.get("email") instanceof String email)
      return new EmailForm(email);

    throw new ErrAPI("email invalid", 400);
  }
}
