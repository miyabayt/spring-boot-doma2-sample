package com.sample.web.admin.controller.html.login;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChangePasswordForm implements Serializable {

    private static final long serialVersionUID = -8779126247823678771L;

    @NotEmpty
    String password;

    @NotEmpty
    String passwordConfirm;

    String token;

    Boolean done;
}
