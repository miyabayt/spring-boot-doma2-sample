package com.sample.web.admin.controller.html.system.staffs;

import static com.sample.common.util.ValidateUtils.isNotEquals;

import com.sample.domain.validator.AbstractValidator;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

/** 担当者登録 入力チェック */
@Component
public class StaffFormValidator extends AbstractValidator<StaffForm> {

  @Override
  protected void doValidate(StaffForm form, Errors errors) {

    // 確認用パスワードと突き合わせる
    if (isNotEquals(form.getPassword(), form.getPasswordConfirm())) {
      errors.rejectValue("password", "staffs.unmatchPassword");
      errors.rejectValue("passwordConfirm", "staffs.unmatchPassword");
    }
  }
}
