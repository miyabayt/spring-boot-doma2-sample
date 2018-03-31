package com.sample.web.admin.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.sample.domain.dto.system.Staff;
import com.sample.web.base.filter.UserIdAware;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginStaff extends User implements UserIdAware {

    private static final long serialVersionUID = 3304847998795623797L;

    private Staff staff = null;

    /**
     * コンストラクタ
     * 
     * @param staff
     * @param authorities
     */
    public LoginStaff(Staff staff, Collection<? extends GrantedAuthority> authorities) {
        super(String.valueOf(staff.getEmail()), staff.getPassword(), authorities);
        this.staff = staff;
    }

    @Override
    public String getUserId() {
        return String.valueOf(this.staff.getId());
    }
}
