package server.paperwork.user_validation.tfa.annotations;

import org.springframework.beans.BeanUtils;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@SuppressFBWarnings({ "REC_CATCH_EXCEPTION" })
public class TFAFormValidator implements ConstraintValidator<TFAFormMatch, Object> {

  private String[] keys;

  @Override
  public void initialize(TFAFormMatch constrAnnt) {
    this.keys = constrAnnt.value();
  }

  @Override
  public boolean isValid(Object form, ConstraintValidatorContext ctx) {
    if (form == null)
      return false;

    try {
      for (String k : keys) {
        var descriptor = BeanUtils.getPropertyDescriptor(form.getClass(), k);
        if (descriptor == null)
          continue;

        var getter = descriptor.getReadMethod();
        if (getter == null)
          continue;

        Object val = getter.invoke(form);

        if (val != null)
          return true;
      }

      return false;
    } catch (Exception err) {
      return false;
    }
  }

}
