package server.paperwork.pair_pwd_form.annotations;

import java.lang.reflect.Method;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@SuppressFBWarnings({ "REC_CATCH_EXCEPTION" })
public class PairPwdFormValidator implements ConstraintValidator<PairPwdFormMatch, Object> {

    @Override
    public boolean isValid(Object form, ConstraintValidatorContext ctx) {
        if (form == null)
            return false;

        try {
            Method getPassword = form.getClass().getMethod("getPassword");
            Method getConfirmPassword = form.getClass().getMethod("getConfirmPassword");

            String password = (String) getPassword.invoke(form);
            String confirmPassword = (String) getConfirmPassword.invoke(form);

            if (password == null || confirmPassword == null)
                return false;

            if (!password.equals(confirmPassword)) {
                ctx.disableDefaultConstraintViolation();

                ctx.buildConstraintViolationWithTemplate("passwords do not match").addPropertyNode("confirmPassword")
                        .addConstraintViolation();
                return false;
            }

            return true;
        } catch (Exception err) {
            return false;
        }
    }
}