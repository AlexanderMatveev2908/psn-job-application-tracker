package server.paperwork.pair_pwd_form.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target(ElementType.TYPE) @Retention(RetentionPolicy.RUNTIME) @Constraint(validatedBy = PairPwdFormValidator.class) @Documented
public @interface PairPwdFormMatch {
    String message() default "passwords do not match";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
