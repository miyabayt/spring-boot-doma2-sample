package com.sample.web.admin.controller.html.system.codes;

import com.sample.domain.validator.AbstractValidator;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

/** コード登録 入力チェック */
@Component
public class CodeFormValidator extends AbstractValidator<CodeForm> {

  @Override
  protected void doValidate(CodeForm form, Errors errors) {}
}
