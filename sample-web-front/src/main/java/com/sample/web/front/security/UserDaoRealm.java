package com.sample.web.front.security;

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

import com.sample.domain.dao.UserDao;
import com.sample.domain.dao.UserPermissionDao;
import com.sample.domain.dto.Permission;
import com.sample.domain.dto.User;
import com.sample.web.base.security.BaseRealm;

import lombok.extern.slf4j.Slf4j;

/**
 * フロント側 認証認可
 */
@Component
@Slf4j
public class UserDaoRealm extends BaseRealm {

    @Autowired
    UserDao userDao;

    @Autowired
    UserPermissionDao permissionDao;

    @Override
    protected UserDetails getLoginUser(String loginId) {

        User user = null;
        List<GrantedAuthority> userAuthorities = null;

        try {
            // login_idをメールアドレスと見立てる
            User where = new User();
            where.setEmail(loginId);

            // 担当者を取得して、セッションに保存する
            user = userDao.select(where)
                    .orElseThrow(() -> new UsernameNotFoundException("no staff found for staff [id=" + loginId + "]"));

            // 権限を取得する
            List<Permission> stafPermissions = permissionDao.selectByUserId(user.getId(), toList());
            Set<String> roles = stafPermissions.stream().map(Permission::getRoleKey).map(r -> {
                if (!r.startsWith("ROLE_")) {
                    return r = "ROLE_" + r;
                }
                return r;
            }).collect(toSet());
            Set<String> permissions = stafPermissions.stream().map(Permission::getPermissionKey).collect(toSet());

            // 役割と権限を両方ともGrantedAuthorityとして渡す
            Set<String> authorities = new HashSet<>();
            authorities.addAll(roles);
            authorities.addAll(permissions);
            userAuthorities = AuthorityUtils.createAuthorityList(authorities.toArray(new String[0]));

        } catch (Exception e) {
            // 0件例外がスローされた場合は何もしない
            // それ以外の例外は、認証エラーの例外で包む
            if (!(e instanceof NoResultException)) {
                throw new UsernameNotFoundException("could not select staff.", e);
            }
        }

        return new UserStaff(user, userAuthorities);
    }
}
