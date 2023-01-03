package com.bigtreetc.sample.domain.validator.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

/** 入力チェック（日付フォーマット） */
@Documented
@Constraint(validatedBy = {DateFormatValidator.class})
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RUNTIME)
public @interface DateFormat {

  String pattern() default "yyyy/MM/dd HH:mm:ss";

  String message() default "{validator.annotation.DateFormat.message}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  @Target({FIELD})
  @Retention(RUNTIME)
  @Documented
  @interface List {
    DateFormat[] value();
  }
}
