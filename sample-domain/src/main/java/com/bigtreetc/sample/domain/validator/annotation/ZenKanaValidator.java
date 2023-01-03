package com.bigtreetc.sample.domain.validator.annotation;

import com.bigtreetc.sample.common.util.ValidateUtils;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/** 入力チェック（全角カナ） */
public class ZenKanaValidator implements ConstraintValidator<ZenKana, String> {

  static final Pattern p = Pattern.compile("^[ァ-ヶ]+$");

  @Override
  public void initialize(ZenKana ZenKana) {}

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    boolean isValid = false;

    if (ValidateUtils.isEmpty(value)) {
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
