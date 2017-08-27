package com.sample.web.admin.security;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.seasar.doma.jdbc.NoResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.sample.domain.dao.StaffDao;
import com.sample.domain.dao.StaffRoleDao;
import com.sample.domain.dto.Permission;
import com.sample.domain.dto.Staff;
import com.sample.web.base.security.BaseRealm;

import lombok.extern.slf4j.Slf4j;

/**
 * 管理側 認証認可
 */
@Component
@Slf4j
public class StaffDaoRealm extends BaseRealm {

    @Autowired
    StaffDao staffDao;

    @Autowired
    StaffRoleDao staffRoleDao;

    @Override
    protected UserDetails getLoginUser(String loginId) {

        Staff staff = null;
        List<GrantedAuthority> authorityList = null;

        try {
            // login_idをメールアドレスと見立てる
            Staff where = new Staff();
            where.setEmail(loginId);

            // 担当者を取得して、セッションに保存する
            staff = staffDao.select(where)
                    .orElseThrow(() -> new UsernameNotFoundException("no staff found [id=" + loginId + "]"));

            // 権限を取得する
            List<Permission> permissions = staffRoleDao.selectByStaffId(staff.getId(), toList());
            Set<String> roles = permissions.stream().map(Permission::getRoleKey).map(r -> {
                if (!r.startsWith("ROLE_")) {
                    r = "ROLE_" + r;
                }
                return r;
            }).collect(toSet());
            Set<String> permissionSet = permissions.stream().map(Permission::getPermissionKey).collect(toSet());

            // 役割と権限を両方ともGrantedAuthorityとして渡す
            Set<String> authorities = new HashSet<>();
            authorities.addAll(roles);
            authorities.addAll(permissionSet);
            authorityList = AuthorityUtils.createAuthorityList(authorities.toArray(new String[0]));

        } catch (Exception e) {
            // 0件例外がスローされた場合は何もしない
            // それ以外の例外は、認証エラーの例外で包む
            if (!(e instanceof NoResultException)) {
                throw new UsernameNotFoundException("could not select staff.", e);
            }
        }

        return new LoginStaff(staff, authorityList);
    }
}
