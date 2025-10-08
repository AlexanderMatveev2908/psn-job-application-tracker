package server.paperwork.user_validation.email_form;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data @JsonIgnoreProperties(ignoreUnknown = true)
public class EmailForm implements EmailInt {
  private String email;
}
