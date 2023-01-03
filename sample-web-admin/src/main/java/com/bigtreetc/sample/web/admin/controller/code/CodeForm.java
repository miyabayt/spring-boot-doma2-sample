package com.bigtreetc.sample.web.admin.controller.code;

import com.bigtreetc.sample.web.base.controller.html.BaseForm;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CodeForm extends BaseForm {

  private static final long serialVersionUID = 7555305356779221873L;

  Long id;

  // 分類コード
  @NotEmpty String categoryCode;

  // コード値
  @NotEmpty String codeValue;

  // コード名
  @NotEmpty String codeName;

  // コードエイリアス
  String codeAlias;

  // 表示順
  @NotNull Integer displayOrder;
}
