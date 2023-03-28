package com.bigtreetc.sample.web.admin.controller.login;

import jakarta.validation.constraints.NotEmpty;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChangePasswordForm implements Serializable {

  private static final long serialVersionUID = -8779126247823678771L;

  @NotEmpty String password;

  @NotEmpty String passwordConfirm;

  String token;

  Boolean done;
}
