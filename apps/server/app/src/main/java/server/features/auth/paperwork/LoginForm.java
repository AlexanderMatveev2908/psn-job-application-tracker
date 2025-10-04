package server.features.auth.paperwork;

import java.util.Map;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.EqualsAndHashCode;
import server.conf.Reg;
import server.paperwork.EmailCheck;

@Data @EqualsAndHashCode(callSuper = true)
public class LoginForm extends EmailCheck {

    public LoginForm(String email, String password) {
        super(email);
        this.password = password;
    }

    @NotBlank(message = "password required") @Pattern(regexp = Reg.PWD, message = "password invalid")
    private final String password;

    public static LoginForm fromMap(Map<String, Object> bd) {
        return new LoginForm((String) bd.get("email"), (String) bd.get("password"));
    }
}
