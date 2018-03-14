package com.sample.domain.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.sample.common.util.ValidateUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 基底入力チェッククラス
 */
@Slf4j
public abstract class AbstractValidator<T> implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void validate(final Object target, final Errors errors) {
        try {
            boolean hasErrors = errors.hasErrors();

            if (!hasErrors || passThruBeanValidation(hasErrors)) {
                // 各機能で実装しているバリデーションを実行する
                doValidate((T) target, errors);
            }
        } catch (RuntimeException e) {
            log.error("validate error", e);
            throw e;
        }
    }

    /**
     * 入力チェックを実施します。
     *
     * @param form
     * @param errors
     */
    protected abstract void doValidate(final T form, final Errors errors);

    /**
     * 相関チェックバリデーションを実施するかどうかを示す値を返します。<br />
     * デフォルトは、JSR-303バリデーションでエラーがあった場合は相関チェックを実施しません。
     * 
     * @return
     */
    protected boolean passThruBeanValidation(boolean hasErrors) {
        return false;
    }

    /**
     * 空であるかどうかを示す値を返します。
     *
     * @param value
     * @return
     */
    protected boolean isEmpty(String value) {
        return ValidateUtils.isEmpty(value);
    }

    /**
     * NULL値であるかどうかを示す値を返します。
     *
     * @param value
     * @return
     */
    protected boolean isNull(Object value) {
        return value == null;
    }

    /**
     * 同一の値であるかどうかを示す値を返します。
     *
     * @param obj1
     * @param obj2
     * @return
     */
    protected boolean equals(Object obj1, Object obj2) {
        return ValidateUtils.equals(obj1, obj2);
    }
}
