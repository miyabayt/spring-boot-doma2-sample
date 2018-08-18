package com.sample.web.admin.controller.html.system.codecategories;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import com.sample.domain.validator.AbstractValidator;

/**
 * コード分類登録 入力チェック
 */
@Component
public class CodeCategoryFormValidator extends AbstractValidator<CodeCategoryForm> {

    @Override
    protected void doValidate(CodeCategoryForm form, Errors errors) {

    }
}
