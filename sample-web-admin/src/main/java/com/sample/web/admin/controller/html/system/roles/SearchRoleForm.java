package com.sample.web.admin.controller.html.system.roles;

import com.sample.web.base.controller.html.BaseSearchForm;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SearchRoleForm extends BaseSearchForm {

  private static final long serialVersionUID = 7979636448439604680L;

  Long id;

  // ロールコード
  String roleCode;

  // ロール名
  String roleName;
}
