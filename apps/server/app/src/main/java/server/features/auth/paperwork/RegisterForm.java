package server.features.auth.paperwork;

import java.util.List;
import java.util.Map;

import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import server.conf.Reg;
import server.decorators.flow.ErrAPI;
import server.paperwork.EmailCheck;
import server.paperwork.PairPwdCheck;

@Data
public class RegisterForm {

    @Valid
    private final EmailCheck emailCheck;

    @Valid
    private final PairPwdCheck pairPwdCheck;

    @NotBlank(message = "first name required") @Pattern(regexp = Reg.NAME, message = "first name invalid")
    private final String firstName;

    @NotBlank(message = "last name required") @Pattern(regexp = Reg.NAME, message = "last name invalid")
    private final String lastName;

    @NotBlank(message = "confirm password required")
    private final String confirmPassword;

    @AssertTrue(message = "terms must be accepted")
    private final Boolean terms;

    public String getEmail() {
        return emailCheck.getEmail();
    }

    public String getPassword() {
        return pairPwdCheck.getPassword();
    }

    public String getConfirmPassword() {
        return pairPwdCheck.getConfirmPassword();
    }

    public RegisterForm(String firstName, String lastName, String email, String password, String confirmPassword,
            Boolean terms) {
        this.emailCheck = new EmailCheck(email);
        this.pairPwdCheck = new PairPwdCheck(password, confirmPassword);
        this.firstName = firstName;
        this.lastName = lastName;
        this.confirmPassword = confirmPassword;
        this.terms = terms;
    }

    public static RegisterForm fromMap(Map<String, Object> bd) {

        var fields = List.of("firstName", "lastName", "email", "password", "confirmPassword");

        for (String f : fields)
            if (!(bd.get(f) instanceof String))
                throw new ErrAPI(String.format("%s invalid", f), 400);

        return new RegisterForm((String) bd.get("firstName"), (String) bd.get("lastName"), (String) bd.get("email"),
                (String) bd.get("password"), (String) bd.get("confirmPassword"),
                Boolean.parseBoolean(String.valueOf(bd.getOrDefault("terms", ""))));
    }
}
