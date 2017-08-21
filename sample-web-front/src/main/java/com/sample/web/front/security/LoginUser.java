package com.sample.web.front.security;

import java.util.Collections;

import com.sample.domain.dto.User;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginUser extends org.springframework.security.core.userdetails.User {

    private static final long serialVersionUID = -5891919297179603893L;

    /**
     * コンストラクタ
     *
     * @param user
     */
    public LoginUser(User user) {
        super(String.valueOf(user.getId().getValue()), user.getPassword(), Collections.emptySet());
    }
}
