package server.features.auth.middleware.register;

import java.util.Map;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterForm {

    @NotBlank(message = "first name required")
    private final String firstName;

    @NotBlank(message = "last name required")
    private final String lastName;

    @NotBlank(message = "email required")
    @Email(message = "invalid email format")
    private final String email;

    @NotBlank(message = "password required")
    private final String password;

    public static RegisterForm mapToForm(Map<String, String> bd) {
        return new RegisterForm(
                bd.get("firstName"),
                bd.get("lastName"),
                bd.get("email"),
                bd.get("password"));
    }
}
