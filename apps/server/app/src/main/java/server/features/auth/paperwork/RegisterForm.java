package server.features.auth.paperwork;

import java.util.List;
import java.util.Map;

import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import lombok.Data;
import server.decorators.flow.ErrAPI;
import server.paperwork.user_validation.EmailForm;
import server.paperwork.user_validation.NamesForm;
import server.paperwork.user_validation.pair_pwd_form.PairPwdForm;

@Data
public class RegisterForm {

    @Valid
    private final EmailForm emailCheck;

    @Valid
    private final PairPwdForm pairPwdCheck;

    @Valid
    private final NamesForm namesCheck;

    @AssertTrue(message = "terms must be accepted")
    private final Boolean terms;

    public RegisterForm(String firstName, String lastName, String email, String password, String confirmPassword,
            Boolean terms) {
        this.emailCheck = new EmailForm(email);
        this.pairPwdCheck = new PairPwdForm(password, confirmPassword);
        this.namesCheck = new NamesForm(firstName, lastName);
        this.terms = terms;
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

    public String getPassword() {
        return pairPwdCheck.getPassword();
    }

    public String getConfirmPassword() {
        return pairPwdCheck.getConfirmPassword();
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
