package server.features.auth.paperwork;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.validation.constraints.AssertTrue;
import lombok.Data;
import server.paperwork.user_validation.NamesSpec;
import server.paperwork.user_validation.email_form.EmailSpec;
import server.paperwork.user_validation.pair_pwd_form.PairPwdSpec;
import server.paperwork.user_validation.pair_pwd_form.annotations.PairPwdFormMatch;

@Data @PairPwdFormMatch @JsonIgnoreProperties(ignoreUnknown = true)
public class RegisterForm implements NamesSpec, EmailSpec, PairPwdSpec {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String confirmPassword;

    @AssertTrue(message = "terms must be accepted")
    private Boolean terms;

}
