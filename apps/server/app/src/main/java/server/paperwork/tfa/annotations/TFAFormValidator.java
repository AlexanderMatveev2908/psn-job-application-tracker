package server.paperwork.tfa.annotations;

import org.springframework.beans.BeanUtils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TFAFormValidator implements ConstraintValidator<TFAFormMatch, Object> {

  private String[] keys;

  @Override
  public void initialize(TFAFormMatch constrAnnt) {
    this.keys = constrAnnt.value();
  }

  @Override
  public boolean isValid(Object form, ConstraintValidatorContext ctx) {
    try {
      for (String k : keys) {
        Object val = BeanUtils.getPropertyDescriptor(form.getClass(), k).getReadMethod().invoke(form);

        if (val != null)
          return true;
      }

      return false;
    } catch (Exception err) {
      return false;
    }
  }

}
