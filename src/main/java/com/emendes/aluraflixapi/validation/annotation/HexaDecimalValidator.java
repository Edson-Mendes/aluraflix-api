package com.emendes.aluraflixapi.validation.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class HexaDecimalValidator implements ConstraintValidator<Hexadecimal, String> {

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (value == null) return true;
    String regex = "[A-Fa-f0-9]*";
    Pattern hexadecimalPattern = Pattern.compile(regex);
    return hexadecimalPattern.matcher(value).matches();
  }

}
