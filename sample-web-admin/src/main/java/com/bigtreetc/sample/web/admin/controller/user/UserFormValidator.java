package com.bigtreetc.sample.web.admin.controller.user;

import com.bigtreetc.sample.common.util.ValidateUtils;
import com.bigtreetc.sample.domain.validator.AbstractValidator;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

/** ユーザー登録 入力チェック */
@Component
public class UserFormValidator extends AbstractValidator<UserForm> {

  @Override
  protected void doValidate(UserForm form, Errors errors) {

    // 確認用パスワードと突き合わせる
    if (ValidateUtils.isNotEquals(form.getPassword(), form.getPasswordConfirm())) {
      errors.rejectValue("password", "users.unmatchPassword");
      errors.rejectValue("passwordConfirm", "users.unmatchPassword");
    }
  }
}
