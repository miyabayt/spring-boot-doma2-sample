package com.bigtreetc.sample.web.admin.controller.role;

import com.bigtreetc.sample.web.base.controller.html.BaseForm;
import java.util.HashMap;
import java.util.Map;
import javax.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RoleForm extends BaseForm {

  private static final long serialVersionUID = 7555305356779221873L;

  Long id;

  // ロールコード
  @NotEmpty String roleCode;

  // ロール名
  @NotEmpty String roleName;

  // 権限
  Map<String, Boolean> permissions = new HashMap<>();
}
