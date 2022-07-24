package com.sample.web.admin.controller.html.system.mailtemplates;

import com.sample.web.base.controller.html.BaseForm;
import javax.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MailTemplateForm extends BaseForm {

  private static final long serialVersionUID = -5860252006532570164L;

  Long id;

  // メールテンプレートキー
  @NotEmpty String templateKey;

  // メールタイトル
  @NotEmpty String subject;

  // メール本文
  @NotEmpty String templateBody;
}
