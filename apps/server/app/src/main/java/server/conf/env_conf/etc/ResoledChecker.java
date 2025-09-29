package server.conf.env_conf.etc;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ResoledChecker implements ConstraintValidator<Resolved, String> {
    @Override
    public boolean isValid(String val, ConstraintValidatorContext ctx) {
        return val != null && !val.isBlank() && !val.contains("${");
    }
}
