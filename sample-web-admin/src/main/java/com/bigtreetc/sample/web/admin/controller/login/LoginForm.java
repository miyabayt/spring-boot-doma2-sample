package com.bigtreetc.sample.web.admin.controller.login;

import jakarta.validation.constraints.NotEmpty;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginForm implements Serializable {

  private static final long serialVersionUID = 7593564324192730932L;

  @NotEmpty String loginId;

  @NotEmpty String password;

  // ログインしたままにするか
  boolean rememberMe;
}
