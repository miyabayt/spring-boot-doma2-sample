package com.sample.web.admin.controller.html.system.staffs;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import com.sample.domain.validator.AbstractValidator;

/**
 * 担当者登録 入力チェック
 */
@Component
public class StaffFormValidator extends AbstractValidator<StaffForm> {

    @Override
    protected void doValidate(StaffForm form, Errors errors) {

        // 確認用パスワードと突き合わせる
        if (!equals(form.getPassword(), form.getPasswordConfirm())) {
            errors.rejectValue("password", "staffs.unmatchPassword");
            errors.rejectValue("passwordConfirm", "staffs.unmatchPassword");
        }
    }
}
