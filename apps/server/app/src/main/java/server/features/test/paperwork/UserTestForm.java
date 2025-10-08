package server.features.test.paperwork;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import server.paperwork.user_validation.NamesInt;
import server.paperwork.user_validation.email_form.EmailInt;

@Data @JsonIgnoreProperties(ignoreUnknown = true)
public class UserTestForm implements NamesInt, EmailInt {

  private String firstName;
  private String lastName;
  private String email;
  private String password;
}