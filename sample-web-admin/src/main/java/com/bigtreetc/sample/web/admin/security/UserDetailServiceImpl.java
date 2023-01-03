package com.bigtreetc.sample.web.admin.security;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import com.bigtreetc.sample.domain.dao.RolePermissionDao;
import com.bigtreetc.sample.domain.dao.StaffDao;
import com.bigtreetc.sample.domain.dao.StaffRoleDao;
import com.bigtreetc.sample.domain.entity.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.seasar.doma.jdbc.SelectOptions;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/** 管理側 認証認可 */
@RequiredArgsConstructor
@Component
@Slf4j
public class UserDetailServiceImpl implements UserDetailsService {

  @NonNull final StaffDao staffDao;

  @NonNull final StaffRoleDao staffRoleDao;

  @NonNull final RolePermissionDao rolePermissionDao;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Staff staff = null;
    List<GrantedAuthority> authorityList = null;

    try {
      val criteria = new StaffCriteria();
      criteria.setEmail(username);

      // 担当者を取得して、セッションに保存する
      staff =
          staffDao
              .select(criteria)
              .orElseThrow(
                  () ->
                      new UsernameNotFoundException("no staff found [username=" + username + "]"));

      // 担当者権限を取得する
      val staffRoles = staffRoleDao.selectByStaffId(staff.getId(), toList());

      // ロールコードにプレフィックスをつけてまとめる
      val roleCodes = staffRoles.stream().map(StaffRole::getRoleCode).collect(toSet());
      val rolePermissions = getRolePermissions(roleCodes);

      // 権限コードをまとめる
      val permissionCodes =
          rolePermissions.stream().map(RolePermission::getPermissionCode).collect(toSet());

      // ロールと権限を両方ともGrantedAuthorityとして渡す
      Set<String> authorities = new HashSet<>();
      for (val roleCode : roleCodes) {
        authorities.add("ROLE_%s".formatted(roleCode));
      }
      authorities.addAll(permissionCodes);
      authorityList = AuthorityUtils.createAuthorityList(authorities.toArray(new String[0]));

      return new LoginStaff(staff, authorityList);

    } catch (Exception e) {
      if (!(e instanceof UsernameNotFoundException)) {
        // 入力間違い以外の例外はログ出力する
        log.error("failed to getLoginUser. ", e);
        throw e;
      }

      // 0件例外がスローされた場合は何もしない
      // それ以外の例外は、認証エラーの例外で包む
      throw new UsernameNotFoundException("could not select staff.", e);
    }
  }

  private List<RolePermission> getRolePermissions(Set<String> roleCodes) {
    val criteria = new RolePermissionCriteria();
    criteria.setRoleCodes(roleCodes);
    criteria.setIsEnabled(true);
    return rolePermissionDao.selectAll(criteria, SelectOptions.get(), toList());
  }
}
