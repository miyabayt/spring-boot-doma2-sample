package com.bigtreetc.sample.web.admin.controller.staff;

import com.bigtreetc.sample.web.base.controller.html.BaseSearchForm;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SearchStaffForm extends BaseSearchForm {

  private static final long serialVersionUID = 4131372368553937515L;

  Long id;

  String firstName;

  String lastName;

  String email;
}
