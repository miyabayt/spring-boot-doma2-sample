package com.sample.domain.validator.annotation;

import static com.sample.common.util.ValidateUtils.isEmpty;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

/** 入力チェック（電話番号） */
@Slf4j
public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {

  private Pattern pattern;

  @Override
  public void initialize(PhoneNumber phoneNumber) {
    try {
      pattern = Pattern.compile(phoneNumber.regexp());
    } catch (PatternSyntaxException e) {
      log.error("invalid regular expression.", e);
      throw e;
    }
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    boolean isValid = false;

    if (isEmpty(value)) {
      isValid = true;
    } else {
      Matcher m = pattern.matcher(value);

      if (m.matches()) {
        isValid = true;
      }
    }

    return isValid;
  }
}
