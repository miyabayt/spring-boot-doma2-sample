package com.sample.web.front.controller.login;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginForm implements Serializable {

    private static final long serialVersionUID = -4165914705498462325L;

    @NotEmpty
    String loginId;

    @NotEmpty
    String password;
}
