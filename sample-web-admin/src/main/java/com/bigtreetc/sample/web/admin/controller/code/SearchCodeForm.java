package com.bigtreetc.sample.web.admin.controller.code;

import com.bigtreetc.sample.web.base.controller.html.BaseSearchForm;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SearchCodeForm extends BaseSearchForm {

  private static final long serialVersionUID = 223278986587249949L;

  Long id;

  String categoryCode;

  String codeValue;

  String codeName;
}
