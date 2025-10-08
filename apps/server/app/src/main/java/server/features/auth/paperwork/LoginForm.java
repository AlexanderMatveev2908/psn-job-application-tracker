package server.features.auth.paperwork;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import server.paperwork.user_validation.email_form.EmailSpec;
import server.paperwork.user_validation.pwd_form.PwdSpec;

@Data @JsonIgnoreProperties(ignoreUnknown = true)
public class LoginForm implements EmailSpec, PwdSpec {
    private String email;
    private String password;
}
