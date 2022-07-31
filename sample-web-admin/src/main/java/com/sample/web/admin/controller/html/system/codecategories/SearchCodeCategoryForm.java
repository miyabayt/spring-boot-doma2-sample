package com.sample.web.admin.controller.html.system.codecategories;

import com.sample.web.base.controller.html.BaseSearchForm;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SearchCodeCategoryForm extends BaseSearchForm {

  private static final long serialVersionUID = -4384562527048697811L;

  Long id;

  String categoryCode;

  String categoryName;
}
