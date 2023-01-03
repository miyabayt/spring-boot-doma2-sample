package com.bigtreetc.sample.web.admin.controller.login;

import com.bigtreetc.sample.common.util.ValidateUtils;
import com.bigtreetc.sample.domain.validator.AbstractValidator;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

/** コード登録 入力チェック */
@Component
public class ChangePasswordFormValidator extends AbstractValidator<ChangePasswordForm> {

  @Override
  protected void doValidate(ChangePasswordForm form, Errors errors) {
    // 確認用パスワードと突き合わせる
    if (ValidateUtils.isNotEquals(form.getPassword(), form.getPasswordConfirm())) {
      errors.rejectValue("password", "changePassword.unmatchPassword");
      errors.rejectValue("passwordConfirm", "changePassword.unmatchPassword");
    }
  }
}
