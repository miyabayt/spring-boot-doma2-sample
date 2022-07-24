package com.sample.web.admin.controller.html.system.roles;

import com.sample.web.base.controller.html.BaseForm;
import java.util.Map;
import javax.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RoleForm extends BaseForm {

  private static final long serialVersionUID = 7555305356779221873L;

  Long id;

  // 役割キー
  @NotEmpty String roleKey;

  // 役割名
  @NotEmpty String roleName;

  // 権限
  Map<Integer, Boolean> permissions;
}
