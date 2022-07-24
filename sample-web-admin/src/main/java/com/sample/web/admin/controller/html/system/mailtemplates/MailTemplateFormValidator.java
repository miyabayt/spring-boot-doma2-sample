package com.sample.web.admin.controller.html.system.mailtemplates;

import com.sample.domain.validator.AbstractValidator;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

/** メールテンプレート登録 入力チェック */
@Component
public class MailTemplateFormValidator extends AbstractValidator<MailTemplateForm> {

  @Override
  protected void doValidate(MailTemplateForm form, Errors errors) {}
}
