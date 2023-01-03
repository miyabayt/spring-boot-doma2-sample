package com.bigtreetc.sample.domain.validator.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

/** 入力チェック（電話番号） */
@Documented
@Constraint(validatedBy = {PhoneNumberValidator.class})
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RUNTIME)
public @interface PhoneNumber {

  String regexp() default "^[0-9]{1,4}-[0-9]{1,4}-[0-9]{4}$";

  String message() default "{validator.annotation.PhoneNumber.message}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  @Target({FIELD})
  @Retention(RUNTIME)
  @Documented
  @interface List {
    PhoneNumber[] value();
  }
}
