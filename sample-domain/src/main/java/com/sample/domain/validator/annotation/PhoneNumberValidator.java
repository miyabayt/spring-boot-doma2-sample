package com.sample.domain.validator.annotation;

import static com.sample.common.util.ValidateUtils.isEmpty;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.hibernate.validator.internal.util.logging.Log;
import org.hibernate.validator.internal.util.logging.LoggerFactory;

/**
 * 入力チェック（電話番号）
 */
public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {

    private static final Log log = LoggerFactory.make();

    private Pattern pattern;

    @Override
    public void initialize(PhoneNumber phoneNumber) {

        try {
            pattern = Pattern.compile(phoneNumber.regexp());
        } catch (PatternSyntaxException e) {
            throw log.getInvalidRegularExpressionException(e);
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
