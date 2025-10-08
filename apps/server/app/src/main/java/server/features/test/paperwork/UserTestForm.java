package server.features.test.paperwork;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import server.paperwork.user_validation.NamesSpec;
import server.paperwork.user_validation.email_form.EmailSpec;

@Data @JsonIgnoreProperties(ignoreUnknown = true)
public class UserTestForm implements NamesSpec, EmailSpec {

  private String firstName;
  private String lastName;
  private String email;
  private String password;
}