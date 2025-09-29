package server.conf.env_conf.etc;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = ResoledChecker.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Resolved {
    String message() default "val contains unresolved placeholder";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}