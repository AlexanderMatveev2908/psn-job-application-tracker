package server.features.auth.paperwork;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PwdChecker implements ConstraintValidator<PwdMatch, RegisterForm> {

    @Override
    public boolean isValid(RegisterForm form, ConstraintValidatorContext ctx) {
        if (form.getPassword() == null || form.getConfirmPassword() == null)
            return false;

        return form.getPassword().equals(form.getConfirmPassword());
    }
}