package server.features.auth.paperwork;

import java.util.Map;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import server.conf.Reg;

@Data
public class LoginForm {

    @NotBlank(message = "email required")
    @Email(message = "email invalid")
    private final String email;

    @NotBlank(message = "password required")
    @Pattern(regexp = Reg.PWD, message = "password invalid")
    private final String password;

    public static LoginForm fromMap(Map<String, Object> bd) {
        return new LoginForm(
                (String) bd.get("email"),
                (String) bd.get("password"));
    }
}
