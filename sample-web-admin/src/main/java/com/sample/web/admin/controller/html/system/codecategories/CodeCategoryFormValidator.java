package com.sample.web.admin.controller.html.system.codecategories;

import com.sample.domain.validator.AbstractValidator;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

/** コード分類登録 入力チェック */
@Component
public class CodeCategoryFormValidator extends AbstractValidator<CodeCategoryForm> {

  @Override
  protected void doValidate(CodeCategoryForm form, Errors errors) {}
}
