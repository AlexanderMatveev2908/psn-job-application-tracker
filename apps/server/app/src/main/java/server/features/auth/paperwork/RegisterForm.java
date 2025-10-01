package server.features.auth.paperwork;

import java.util.Map;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.EqualsAndHashCode;
import server.conf.Reg;
import server.features.auth.paperwork.pwd.PwdMatch;

@Data
@EqualsAndHashCode(callSuper = true)
@PwdMatch
public class RegisterForm extends LoginForm {

    public RegisterForm(
            String firstName,
            String lastName,
            String email,
            String password,
            String confirmPassword,
            Boolean terms) {
        super(email, password);
        this.firstName = firstName;
        this.lastName = lastName;
        this.confirmPassword = confirmPassword;
        this.terms = terms;
    }

    @NotBlank(message = "first name required")
    @Pattern(regexp = Reg.NAME, message = "first name invalid")
    private final String firstName;

    @NotBlank(message = "last name required")
    @Pattern(regexp = Reg.NAME, message = "last name invalid")
    private final String lastName;

    @NotBlank(message = "confirm password required")
    private final String confirmPassword;

    @AssertTrue(message = "terms must be accepted")
    private final Boolean terms;

    public static RegisterForm fromMap(Map<String, Object> bd) {
        return new RegisterForm(
                (String) bd.get("firstName"),
                (String) bd.get("lastName"),
                (String) bd.get("email"),
                (String) bd.get("password"),
                (String) bd.get("confirmPassword"),
                Boolean.parseBoolean(String.valueOf(bd.getOrDefault("terms", ""))));
    }
}
