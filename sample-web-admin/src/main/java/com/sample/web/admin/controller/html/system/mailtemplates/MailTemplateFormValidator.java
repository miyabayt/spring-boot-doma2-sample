package com.sample.web.admin.controller.html.system.mailtemplates;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import com.sample.domain.validator.AbstractValidator;

/**
 * メールテンプレート登録 入力チェック
 */
@Component
public class MailTemplateFormValidator extends AbstractValidator<MailTemplateForm> {

    @Override
    protected void doValidate(MailTemplateForm form, Errors errors) {

    }
}
