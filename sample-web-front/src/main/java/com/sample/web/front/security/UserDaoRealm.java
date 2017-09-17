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

import com.sample.domain.dao.user.UserDao;
import com.sample.domain.dao.user.UserRoleDao;
import com.sample.domain.dto.user.User;
import com.sample.domain.dto.user.UserRole;
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
    UserRoleDao userRoleDao;

    @Override
    protected UserDetails getLoginUser(String loginId) {
        User user = null;
        List<GrantedAuthority> authorityList = null;

        try {
            // login_idをメールアドレスと見立てる
            User where = new User();
            where.setEmail(loginId);

            // ユーザーを取得して、セッションに保存する
            user = userDao.select(where)
                    .orElseThrow(() -> new UsernameNotFoundException("no user found. [id=" + loginId + "]"));

            // 担当者権限を取得する
            List<UserRole> userRoles = userRoleDao.selectByUserId(user.getId(), toList());

            // 役割キーにプレフィックスをつけてまとめる
            Set<String> roleKeys = userRoles.stream().map(UserRole::getRoleKey).collect(toSet());

            // 権限キーをまとめる
            Set<String> permissionKeys = userRoles.stream().map(UserRole::getPermissionKey).collect(toSet());

            // 役割と権限を両方ともGrantedAuthorityとして渡す
            Set<String> authorities = new HashSet<>();
            authorities.addAll(roleKeys);
            authorities.addAll(permissionKeys);
            authorityList = AuthorityUtils.createAuthorityList(authorities.toArray(new String[0]));

        } catch (Exception e) {
            // 0件例外がスローされた場合は何もしない
            // それ以外の例外は、認証エラーの例外で包む
            if (!(e instanceof NoResultException)) {
                throw new UsernameNotFoundException("could not select user.", e);
            }
        }

        return new LoginUser(user, authorityList);
    }
}
