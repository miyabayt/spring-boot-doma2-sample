package com.bigtreetc.sample.web.admin.controller.mailtemplate;

import com.bigtreetc.sample.web.base.controller.html.BaseForm;
import javax.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MailTemplateForm extends BaseForm {

  private static final long serialVersionUID = -5860252006532570164L;

  Long id;

  // メールテンプレートコード
  @NotEmpty String templateCode;

  // メールタイトル
  @NotEmpty String subject;

  // メール本文
  @NotEmpty String templateBody;
}
