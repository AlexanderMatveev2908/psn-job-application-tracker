package server.paperwork;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import server.conf.Reg;

@Data
public class NamesCheck {
  @NotBlank(message = "first name required") @Pattern(regexp = Reg.NAME, message = "first name invalid")
  private final String firstName;

  @NotBlank(message = "last name required") @Pattern(regexp = Reg.NAME, message = "last name invalid")
  private final String lastName;

}
