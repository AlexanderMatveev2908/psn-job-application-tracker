package server.paperwork.user_validation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import server.conf.Reg;

public interface NamesSpec {
  @NotBlank(message = "first name required") @Pattern(regexp = Reg.NAME, message = "first name invalid") @Size(max = 100, message = "first name must be between 1 and 100 characters")
  String getFirstName();

  @NotBlank(message = "last name required") @Pattern(regexp = Reg.NAME, message = "last name invalid") @Size(max = 100, message = "last name must be between 1 and 100 characters")
  String getLastName();
}
