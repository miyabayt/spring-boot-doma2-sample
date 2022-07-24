package com.sample.web.admin.controller.html.system.codes;

import com.sample.web.base.controller.html.BaseForm;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CodeForm extends BaseForm {

  private static final long serialVersionUID = 7555305356779221873L;

  Long id;

  // コードキー
  @NotEmpty String codeKey;

  // コード名
  @NotEmpty String codeValue;

  // コードエイリアス
  String codeAlias;

  // 表示順
  @NotNull Integer displayOrder;

  // 無効フラグ
  Boolean isInvalid;
}
