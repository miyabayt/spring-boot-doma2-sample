package com.bigtreetc.sample.web.admin.controller.codecategory;

import com.bigtreetc.sample.web.base.controller.html.BaseForm;
import javax.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CodeCategoryForm extends BaseForm {

  private static final long serialVersionUID = -7942742528754164062L;

  Long id;

  // 分類コード
  @NotEmpty String categoryCode;

  // 分類名
  @NotEmpty String categoryName;
}
