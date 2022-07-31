package com.sample.web.admin.controller.html.users.users;

import com.sample.web.base.controller.html.BaseSearchForm;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SearchUserForm extends BaseSearchForm {

  private static final long serialVersionUID = 4131372368553937515L;

  Long id;

  String firstName;

  String lastName;

  String email;
}
