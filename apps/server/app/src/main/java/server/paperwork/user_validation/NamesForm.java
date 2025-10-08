package server.paperwork.user_validation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import server.conf.Reg;

@Data
public class NamesForm {
  @NotBlank(message = "first name required") @Pattern(regexp = Reg.NAME, message = "first name invalid") @Size(min = 1, max = 100, message = "first name must be between 1 and 100 char")
  private final String firstName;

  @NotBlank(message = "last name required") @Pattern(regexp = Reg.NAME, message = "last name invalid") @Size(min = 1, max = 100, message = "last name must be between 1 and 100 char")
  private final String lastName;

}
