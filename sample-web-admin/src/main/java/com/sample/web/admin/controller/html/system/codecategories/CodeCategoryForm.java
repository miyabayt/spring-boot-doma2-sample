package com.sample.web.admin.controller.html.system.codecategories;

import com.sample.web.base.controller.html.BaseForm;
import javax.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CodeCategoryForm extends BaseForm {

  private static final long serialVersionUID = -7942742528754164062L;

  Long id;

  // コード分類キー
  @NotEmpty String categoryKey;

  // コード分類名
  @NotEmpty String categoryName;
}
