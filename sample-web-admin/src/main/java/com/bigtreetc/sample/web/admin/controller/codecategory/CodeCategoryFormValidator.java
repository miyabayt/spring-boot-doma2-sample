package com.bigtreetc.sample.web.admin.controller.codecategory;

import com.bigtreetc.sample.domain.validator.AbstractValidator;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

/** コード分類登録 入力チェック */
@Component
public class CodeCategoryFormValidator extends AbstractValidator<CodeCategoryForm> {

  @Override
  protected void doValidate(CodeCategoryForm form, Errors errors) {}
}
