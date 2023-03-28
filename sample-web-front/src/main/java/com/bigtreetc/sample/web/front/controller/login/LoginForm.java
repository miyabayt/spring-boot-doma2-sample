package com.bigtreetc.sample.web.front.controller.login;

import jakarta.validation.constraints.NotEmpty;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginForm implements Serializable {

  private static final long serialVersionUID = -4165914705498462325L;

  @NotEmpty String loginId;

  @NotEmpty String password;
}
