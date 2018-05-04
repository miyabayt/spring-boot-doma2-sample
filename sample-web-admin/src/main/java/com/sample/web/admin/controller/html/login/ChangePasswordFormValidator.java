package com.sample.web.admin.controller.html.login;

import static com.sample.common.util.ValidateUtils.isNotEquals;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import com.sample.domain.validator.AbstractValidator;

/**
 * コード登録 入力チェック
 */
@Component
public class ChangePasswordFormValidator extends AbstractValidator<ChangePasswordForm> {

    @Override
    protected void doValidate(ChangePasswordForm form, Errors errors) {
        // 確認用パスワードと突き合わせる
        if (isNotEquals(form.getPassword(), form.getPasswordConfirm())) {
            errors.rejectValue("password", "changePassword.unmatchPassword");
            errors.rejectValue("passwordConfirm", "changePassword.unmatchPassword");
        }
    }
}
