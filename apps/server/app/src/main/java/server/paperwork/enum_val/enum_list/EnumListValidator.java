package server.paperwork.enum_val.enum_list;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EnumListValidator implements ConstraintValidator<EnumList, Object> {

  private Set<String> whitelist;

  @Override
  public void initialize(EnumList annt) {
    whitelist = Arrays.stream(annt.enumCls().getEnumConstants()).map(Enum::name).collect(Collectors.toSet());

  }

  @Override
  public boolean isValid(Object arr, ConstraintValidatorContext ctx) {
    if (arr == null)
      return true;

    if (!(arr instanceof List))
      return false;

    List<?> casted = (List<?>) arr;

    if (casted.isEmpty())
      return true;

    for (Object val : casted) {
      if (!(val instanceof String str))
        return false;

      if (!whitelist.contains(str))
        return false;
    }

    return true;
  }

}
