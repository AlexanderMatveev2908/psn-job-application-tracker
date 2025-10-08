package server.paperwork.enum_val;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EnumMatchValidator implements ConstraintValidator<EnumMatch, Object> {
  private Set<String> whitelist;

  @Override
  public void initialize(EnumMatch annt) {
    whitelist = Arrays.stream(annt.enumCls().getEnumConstants()).map(Enum::name).collect(Collectors.toSet());
  }

  @Override
  public boolean isValid(Object value, ConstraintValidatorContext context) {
    if (value == null)
      return false;

    return whitelist.contains(value.toString());
  }
}
