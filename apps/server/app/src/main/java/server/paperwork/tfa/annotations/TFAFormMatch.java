package server.paperwork.tfa.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target(ElementType.TYPE) @Retention(RetentionPolicy.RUNTIME) @Constraint(validatedBy = TFAFormValidator.class)
public @interface TFAFormMatch {
  String message() default "neither totp_code nor backup_code has been provided";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  String[] value();
}