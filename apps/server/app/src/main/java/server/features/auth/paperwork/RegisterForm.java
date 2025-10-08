package server.features.auth.paperwork;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.validation.constraints.AssertTrue;
import lombok.Data;
import lombok.EqualsAndHashCode;
import server.paperwork.user_validation.NamesInt;
import server.paperwork.user_validation.email_form.EmailInt;
import server.paperwork.user_validation.pair_pwd_form.PairPwdForm;

@Data @EqualsAndHashCode(callSuper = true) @JsonIgnoreProperties(ignoreUnknown = true)
public class RegisterForm extends PairPwdForm implements NamesInt, EmailInt {

    private String firstName;
    private String lastName;
    private String email;

    @AssertTrue(message = "terms must be accepted")
    private Boolean terms;

}
