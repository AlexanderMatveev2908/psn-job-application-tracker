package server.features.auth.paperwork;

import java.util.Map;

import jakarta.validation.Valid;
import lombok.Data;
import server.paperwork.EmailCheck;
import server.paperwork.PwdCheck;

@Data
public class LoginForm {

    @Valid
    private final EmailCheck emailCheck;

    @Valid
    private final PwdCheck pwdCheck;

    public LoginForm(String email, String password) {
        this.emailCheck = new EmailCheck(email);
        this.pwdCheck = new PwdCheck(password);
    }

    public LoginForm(EmailCheck emailCheck, PwdCheck pwdCheck) {
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
        return new LoginForm(EmailCheck.fromBody(bd), PwdCheck.fromBody(bd));
    }
}
