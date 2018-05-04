package com.sample.web.admin.controller.html.users.users;

import static com.sample.common.util.ValidateUtils.isNotEquals;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import com.sample.domain.validator.AbstractValidator;

/**
 * ユーザー登録 入力チェック
 */
@Component
public class UserFormValidator extends AbstractValidator<UserForm> {

    @Override
    protected void doValidate(UserForm form, Errors errors) {

        // 確認用パスワードと突き合わせる
        if (isNotEquals(form.getPassword(), form.getPasswordConfirm())) {
            errors.rejectValue("password", "users.unmatchPassword");
            errors.rejectValue("passwordConfirm", "users.unmatchPassword");
        }
    }
}
