package com.bigtreetc.sample.web.admin.controller.code;

import com.bigtreetc.sample.domain.validator.AbstractValidator;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

/** コードマスタ登録 入力チェック */
@Component
public class CodeFormValidator extends AbstractValidator<CodeForm> {

  @Override
  protected void doValidate(CodeForm form, Errors errors) {}
}
