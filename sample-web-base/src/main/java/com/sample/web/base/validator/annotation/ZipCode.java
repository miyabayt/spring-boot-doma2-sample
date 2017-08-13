package com.sample.web.base.validator.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * 入力チェック（郵便番号）
 */
@Documented
@Constraint(validatedBy = { ZipCodeValidator.class })
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RUNTIME)
public @interface ZipCode {

    String message() default "{validator.annotation.ZipCode.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({ FIELD })
    @Retention(RUNTIME)
    @Documented
    @interface List {
        ZipCode[] value();
    }
}
