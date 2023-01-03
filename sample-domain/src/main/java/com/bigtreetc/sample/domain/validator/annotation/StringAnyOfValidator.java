package com.bigtreetc.sample.domain.validator.annotation;

import java.util.Arrays;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

/** 入力チェック（指定した値のいずれか） */
@Slf4j
public class StringAnyOfValidator implements ConstraintValidator<StringAnyOf, String> {

  private String[] values;

  @Override
  public void initialize(StringAnyOf anyOf) {
    values = anyOf.values();
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    boolean isValid;

    if (value == null) {
      isValid = true;
    } else {
      isValid = Arrays.asList(values).contains(value);
    }

    return isValid;
  }
}
