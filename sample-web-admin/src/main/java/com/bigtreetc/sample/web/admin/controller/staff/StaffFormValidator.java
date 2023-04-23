package com.bigtreetc.sample.web.admin.controller.staff;

import com.bigtreetc.sample.common.util.ValidateUtils;
import com.bigtreetc.sample.domain.validator.AbstractValidator;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

/** 担当者マスタ登録 入力チェック */
@Component
public class StaffFormValidator extends AbstractValidator<StaffForm> {

  @Override
  protected void doValidate(StaffForm form, Errors errors) {

    // 確認用パスワードと突き合わせる
    if (ValidateUtils.isNotEquals(form.getPassword(), form.getPasswordConfirm())) {
      errors.rejectValue("password", "staffs.unmatchPassword");
      errors.rejectValue("passwordConfirm", "staffs.unmatchPassword");
    }
  }
}
