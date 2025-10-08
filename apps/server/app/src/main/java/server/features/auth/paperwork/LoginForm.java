package server.features.auth.paperwork;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import server.lib.data_structure.parser.Prs;
import server.paperwork.user_validation.email_form.EmailInt;

@Data @JsonIgnoreProperties(ignoreUnknown = true)
public class LoginForm implements EmailInt {

    private String email;
    private String password;

    public static LoginForm fromMap(Map<String, Object> bd) {
        return Prs.fromMapToT(bd, LoginForm.class);
    }
}
