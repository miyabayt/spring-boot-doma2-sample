package com.bigtreetc.sample.web.api.security;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import com.bigtreetc.sample.domain.dao.UserDao;
import com.bigtreetc.sample.domain.dao.UserRoleDao;
import com.bigtreetc.sample.domain.entity.User;
import com.bigtreetc.sample.domain.entity.UserCriteria;
import com.bigtreetc.sample.domain.entity.UserRole;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.seasar.doma.jdbc.NoResultException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/** API認証認可 */
@RequiredArgsConstructor
@Component
@Slf4j
public class UserDetailServiceImpl implements UserDetailsService {

  @NonNull final UserDao userDao;

  @NonNull final UserRoleDao userRoleDao;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = null;
    List<GrantedAuthority> authorityList = null;

    try {
      val criteria = new UserCriteria();
      criteria.setEmail(username);

      // ユーザーを取得して、セッションに保存する
      user =
          userDao
              .select(criteria)
              .orElseThrow(
                  () ->
                      new UsernameNotFoundException("no user found. [username=" + username + "]"));

      // 担当者権限を取得する
      val userRoles = userRoleDao.selectByUserId(user.getId(), toList());

      // ロールコードにプレフィックスをつけてまとめる
      val roleCodes = userRoles.stream().map(UserRole::getRoleCode).collect(toSet());

      // 権限コードをまとめる
      val permissionCodes = userRoles.stream().map(UserRole::getPermissionCode).collect(toSet());

      // ロールと権限を両方ともGrantedAuthorityとして渡す
      Set<String> authorities = new HashSet<>();
      for (val roleCode : roleCodes) {
        authorities.add("ROLE_%s".formatted(roleCode));
      }
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
