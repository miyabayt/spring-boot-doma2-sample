package com.sample.web.front.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

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
     * @param authorities
     */
    public LoginUser(User user, Collection<? extends GrantedAuthority> authorities) {
        super(String.valueOf(user.getId().getValue()), user.getPassword(), authorities);
    }
}
