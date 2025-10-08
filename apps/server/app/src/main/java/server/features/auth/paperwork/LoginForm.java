package server.features.auth.paperwork;

import java.util.Map;

import jakarta.validation.Valid;
import lombok.Data;
import server.paperwork.user_validation.EmailForm;
import server.paperwork.user_validation.PwdForm;

@Data
public class LoginForm {

    @Valid
    private final EmailForm emailCheck;

    @Valid
    private final PwdForm pwdCheck;

    public LoginForm(String email, String password) {
        this.emailCheck = new EmailForm(email);
        this.pwdCheck = new PwdForm(password);
    }

    public LoginForm(EmailForm emailCheck, PwdForm pwdCheck) {
        this.emailCheck = emailCheck;
        this.pwdCheck = pwdCheck;
    }

    public String getEmail() {
        return emailCheck.getEmail();
    }

    public String getPassword() {
        return pwdCheck.getPassword();
    }

    public static LoginForm fromMap(Map<String, Object> bd) {
        return new LoginForm(EmailForm.fromBody(bd), PwdForm.fromBody(bd));
    }
}
