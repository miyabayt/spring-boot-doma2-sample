package com.bigtreetc.sample.web.admin.controller.role;

import com.bigtreetc.sample.domain.validator.AbstractValidator;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

/** ロールマスタ登録 入力チェック */
@Component
public class RoleFormValidator extends AbstractValidator<RoleForm> {

  @Override
  protected void doValidate(RoleForm form, Errors errors) {}
}
