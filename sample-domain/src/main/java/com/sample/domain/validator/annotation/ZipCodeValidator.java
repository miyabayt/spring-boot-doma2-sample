package com.sample.domain.validator.annotation;

import static com.sample.common.util.ValidateUtils.isEmpty;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/** 入力チェック（郵便番号） */
public class ZipCodeValidator implements ConstraintValidator<ZipCode, String> {

  static final Pattern p = Pattern.compile("^[0-9]{7}$");

  @Override
  public void initialize(ZipCode ZipCode) {}

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    boolean isValid = false;

    if (isEmpty(value)) {
      isValid = true;
    } else {
      Matcher m = p.matcher(value);

      if (m.matches()) {
        isValid = true;
      }
    }

    return isValid;
  }
}
