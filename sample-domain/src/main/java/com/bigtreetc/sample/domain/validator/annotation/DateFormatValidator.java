package com.bigtreetc.sample.domain.validator.annotation;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

/** 入力チェック（日付フォーマット） */
@Slf4j
public class DateFormatValidator implements ConstraintValidator<DateFormat, String> {

  private DateTimeFormatter pattern;

  @Override
  public void initialize(DateFormat dateFormat) {
    pattern =
        DateTimeFormatter.ofPattern(dateFormat.pattern()).withResolverStyle(ResolverStyle.LENIENT);
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    boolean isValid = false;

    if (value == null) {
      isValid = true;
    } else {
      try {
        pattern.parse(value);
      } catch (DateTimeParseException e) {
        // ignore
      }
    }

    return isValid;
  }
}
