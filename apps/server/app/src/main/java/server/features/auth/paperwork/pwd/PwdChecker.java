package server.features.auth.paperwork.pwd;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import server.features.auth.paperwork.RegisterForm;

public class PwdChecker implements ConstraintValidator<PwdMatch, RegisterForm> {

    @Override
    public boolean isValid(RegisterForm form, ConstraintValidatorContext ctx) {
        if (form.getPassword() == null || form.getConfirmPassword() == null)
            return false;

        if (!form.getPassword().equals(form.getConfirmPassword())) {
            ctx.disableDefaultConstraintViolation();

            ctx.buildConstraintViolationWithTemplate("passwords do not match")
                    .addPropertyNode("confirmPassword")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}