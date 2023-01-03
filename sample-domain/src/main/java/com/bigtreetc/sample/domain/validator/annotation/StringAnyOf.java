package com.bigtreetc.sample.domain.validator.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

/** 入力チェック（指定した値のいずれか） */
@Documented
@Constraint(validatedBy = {StringAnyOfValidator.class})
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RUNTIME)
public @interface StringAnyOf {

  String[] values() default {};

  String message() default "{validator.annotation.StringAnyOf.message}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  @Target({FIELD})
  @Retention(RUNTIME)
  @Documented
  @interface List {
    StringAnyOf[] value();
  }
}
