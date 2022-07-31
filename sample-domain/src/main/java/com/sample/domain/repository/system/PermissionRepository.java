package com.sample.domain.repository.system;

import static com.sample.domain.util.DomaUtils.createSelectOptions;
import static java.util.stream.Collectors.toList;

import com.sample.domain.dao.system.PermissionDao;
import com.sample.domain.dto.system.Permission;
import com.sample.domain.dto.system.PermissionCriteria;
import com.sample.domain.exception.NoDataFoundException;
import java.util.Optional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

/** 権限リポジトリ */
@RequiredArgsConstructor
@Repository
public class PermissionRepository {

  @NonNull final PermissionDao permissionDao;

  /**
   * 権限を複数取得します。
   *
   * @param criteria
   * @param pageable
   * @return
   */
  public Page<Permission> findAll(PermissionCriteria criteria, Pageable pageable) {
    val options = createSelectOptions(pageable).count();
    val data = permissionDao.selectAll(criteria, options, toList());
    return new PageImpl<>(data, pageable, options.getCount());
  }

  /**
   * 権限を取得します。
   *
   * @param criteria
   * @return
   */
  public Optional<Permission> findOne(PermissionCriteria criteria) {
    return permissionDao.select(criteria);
  }

  /**
   * 権限を取得します。
   *
   * @return
   */
  public Permission findById(final Long id) {
    return permissionDao
        .selectById(id)
        .orElseThrow(() -> new NoDataFoundException("permission_id=" + id + " のデータが見つかりません。"));
  }

  /**
   * 権限を追加します。
   *
   * @param inputPermission
   * @return
   */
  public Permission create(final Permission inputPermission) {
    permissionDao.insert(inputPermission);

    return inputPermission;
  }

  /**
   * 権限を更新します。
   *
   * @param inputPermission
   * @return
   */
  public Permission update(final Permission inputPermission) {
    int updated = permissionDao.update(inputPermission);

    if (updated < 1) {
      throw new NoDataFoundException("permission_id=" + inputPermission.getId() + " のデータが見つかりません。");
    }

    return inputPermission;
  }

  /**
   * 権限を論理削除します。
   *
   * @return
   */
  public Permission delete(final Long id) {
    val permission =
        permissionDao
            .selectById(id)
            .orElseThrow(() -> new NoDataFoundException("permission_id=" + id + " のデータが見つかりません。"));

    int updated = permissionDao.delete(permission);

    if (updated < 1) {
      throw new NoDataFoundException("permission_id=" + id + " は更新できませんでした。");
    }

    return permission;
  }
}
