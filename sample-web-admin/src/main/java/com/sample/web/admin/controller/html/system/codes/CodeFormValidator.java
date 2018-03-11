package com.sample.web.admin.controller.html.system.codes;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import com.sample.domain.validator.AbstractValidator;

/**
 * コード登録 入力チェック
 */
@Component
public class CodeFormValidator extends AbstractValidator<CodeForm> {

    @Override
    protected void doValidate(CodeForm form, Errors errors) {

    }
}
