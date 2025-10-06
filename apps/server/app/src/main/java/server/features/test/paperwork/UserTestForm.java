package server.features.test.paperwork;

import java.util.List;
import java.util.Map;

import jakarta.validation.Valid;
import lombok.Data;
import server.decorators.flow.ErrAPI;
import server.paperwork.EmailCheck;
import server.paperwork.NamesCheck;

@Data
public class UserTestForm {

  @Valid
  private final EmailCheck emailCheck;

  @Valid
  private final NamesCheck namesCheck;

  public UserTestForm(String firstName, String lastName, String email, String password) {
    this.emailCheck = new EmailCheck(email);
    this.namesCheck = new NamesCheck(firstName, lastName);
  }

  public String getFirstName() {
    return namesCheck.getFirstName();
  }

  public String getLastName() {
    return namesCheck.getLastName();
  }

  public String getEmail() {
    return emailCheck.getEmail();
  }

  public static UserTestForm fromMap(Map<String, Object> bd) {

    var fields = List.of("firstName", "lastName", "email", "password");

    for (String f : fields)
      if (!(bd.get(f) instanceof String))
        throw new ErrAPI(String.format("%s invalid", f), 400);

    return new UserTestForm((String) bd.get("firstName"), (String) bd.get("lastName"), (String) bd.get("email"),
        (String) bd.get("password"));
  }

}
