package com.sample.web.admin.controller.html.system.mailtemplates;

import com.sample.web.base.controller.html.BaseSearchForm;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SearchMailTemplateForm extends BaseSearchForm {

  private static final long serialVersionUID = -6365336122351427141L;

  Long id;

  // メールテンプレートコード
  String templateCode;

  // メールタイトル
  String subject;

  // メール本文
  String templateBody;
}
