package com.sample.web.front.security;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import com.sample.domain.dao.users.UserDao;
import com.sample.domain.dao.users.UserRoleDao;
import com.sample.domain.dto.user.User;
import com.sample.domain.dto.user.UserCriteria;
import com.sample.domain.dto.user.UserRole;
import com.sample.web.base.security.BaseRealm;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.seasar.doma.jdbc.NoResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/** フロント側 認証認可 */
@Component
@Slf4j
public class UserDaoRealm extends BaseRealm {

  @Autowired UserDao userDao;

  @Autowired UserRoleDao userRoleDao;

  @Override
  protected UserDetails getLoginUser(String loginId) {
    User user = null;
    List<GrantedAuthority> authorityList = null;

    try {
      // login_idをメールアドレスと見立てる
      val criteria = new UserCriteria();
      criteria.setEmail(loginId);

      // ユーザーを取得して、セッションに保存する
      user =
          userDao
              .select(criteria)
              .orElseThrow(
                  () -> new UsernameNotFoundException("no user found. [id=" + loginId + "]"));

      // 担当者権限を取得する
      List<UserRole> userRoles = userRoleDao.selectByUserId(user.getId(), toList());

      // ロールコードにプレフィックスをつけてまとめる
      Set<String> roleCodes = userRoles.stream().map(UserRole::getRoleCode).collect(toSet());

      // 権限コードをまとめる
      Set<String> permissionCodes =
          userRoles.stream().map(UserRole::getPermissionCode).collect(toSet());

      // ロールと権限を両方ともGrantedAuthorityとして渡す
      Set<String> authorities = new HashSet<>();
      authorities.addAll(roleCodes);
      authorities.addAll(permissionCodes);
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
