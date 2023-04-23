package com.bigtreetc.sample.domain.repository;

import static com.bigtreetc.sample.domain.util.DomaUtils.createSelectOptions;
import static java.util.stream.Collectors.toList;

import com.bigtreetc.sample.common.util.ValidateUtils;
import com.bigtreetc.sample.domain.dao.RoleDao;
import com.bigtreetc.sample.domain.dao.RolePermissionDao;
import com.bigtreetc.sample.domain.entity.*;
import com.bigtreetc.sample.domain.exception.NoDataFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

/** ロールマスタリポジトリ */
@RequiredArgsConstructor
@Repository
public class RoleRepository {

  @NonNull final RoleDao roleDao;

  @NonNull final RolePermissionDao rolePermissionDao;

  /**
   * ロールマスタを検索します。
   *
   * @param criteria
   * @param pageable
   * @return
   */
  public Page<Role> findAll(RoleCriteria criteria, Pageable pageable) {
    val options = createSelectOptions(pageable).count();
    val data = roleDao.selectAll(criteria, options, toList());
    return new PageImpl<>(data, pageable, options.getCount());
  }

  /**
   * ロールマスタを検索します。
   *
   * @return
   */
  public Stream<Role> findAll(RoleCriteria criteria) {
    return roleDao.selectAll(criteria);
  }

  /**
   * ロールマスタを検索します。
   *
   * @param criteria
   * @return
   */
  public Optional<Role> findOne(RoleCriteria criteria) {
    val role = roleDao.select(criteria);

    role.ifPresent(
        r -> {
          val rolePermissions = findRolePermissions(r);
          if (ValidateUtils.isNotEmpty(rolePermissions)) {
            r.getRolePermissions().addAll(rolePermissions);
          }
        });

    return role;
  }

  /**
   * ロールを取得します。
   *
   * @return
   */
  public Role findById(final Long id) {
    val role =
        roleDao
            .selectById(id)
            .orElseThrow(() -> new NoDataFoundException("role_id=" + id + " のデータが見つかりません。"));

    val rolePermissions = findRolePermissions(role);
    if (ValidateUtils.isNotEmpty(rolePermissions)) {
      role.getRolePermissions().addAll(rolePermissions);
    }

    return role;
  }

  /**
   * ロールを追加します。
   *
   * @param inputRole
   * @return
   */
  public Role create(final Role inputRole) {
    roleDao.insert(inputRole);

    // ロール権限紐付けを登録する
    val rolePermissions = inputRole.getRolePermissions();
    rolePermissionDao.insert(rolePermissions);

    return inputRole;
  }

  /**
   * ロールを更新します。
   *
   * @param inputRole
   * @return
   */
  public Role update(final Role inputRole) {
    int updated = roleDao.update(inputRole);

    if (updated < 1) {
      throw new NoDataFoundException("role_id=" + inputRole.getId() + " のデータが見つかりません。");
    }

    // ロール権限紐付けを更新する
    val roleCode = inputRole.getRoleCode();
    val rolePermissions = inputRole.getRolePermissions();
    for (val rp : rolePermissions) {
      rp.setRoleCode(roleCode);
      rolePermissionDao.update(rp);
    }

    return inputRole;
  }

  /**
   * ロールを論理削除します。
   *
   * @return
   */
  public Role delete(final Long id) {
    val role =
        roleDao
            .selectById(id)
            .orElseThrow(() -> new NoDataFoundException("role_id=" + id + " のデータが見つかりません。"));

    int updated = roleDao.delete(role);

    if (updated < 1) {
      throw new NoDataFoundException("role_id=" + id + " は更新できませんでした。");
    }

    // ロール権限紐付けを論理削除する
    deleteRolePermissions(role);

    return role;
  }

  /**
   * ロール権限紐付けを論理削除する
   *
   * @param inputRole
   */
  protected void deleteRolePermissions(final Role inputRole) {
    List<RolePermission> rolePermissionsToDelete = findRolePermissions(inputRole);

    if (ValidateUtils.isNotEmpty(rolePermissionsToDelete)) {
      rolePermissionDao.delete(rolePermissionsToDelete); // 一括論理削除
    }
  }

  /**
   * ロール権限紐付けを取得する
   *
   * @param inputRole
   * @return
   */
  protected List<RolePermission> findRolePermissions(Role inputRole) {
    // ロール権限紐付けをロールコードで取得する
    val criteria = new RolePermissionCriteria();
    criteria.setRoleCode(inputRole.getRoleCode());

    val options = createSelectOptions(Pageable.unpaged());
    return rolePermissionDao.selectAll(criteria, options, toList());
  }
}
