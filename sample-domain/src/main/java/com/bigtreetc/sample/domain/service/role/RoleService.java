package com.bigtreetc.sample.domain.service.role;

import com.bigtreetc.sample.domain.entity.Permission;
import com.bigtreetc.sample.domain.entity.PermissionCriteria;
import com.bigtreetc.sample.domain.entity.Role;
import com.bigtreetc.sample.domain.entity.RoleCriteria;
import com.bigtreetc.sample.domain.repository.PermissionRepository;
import com.bigtreetc.sample.domain.repository.RoleRepository;
import com.bigtreetc.sample.domain.service.BaseTransactionalService;
import com.bigtreetc.sample.domain.util.CsvUtils;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Optional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/** ロールマスタサービス */
@RequiredArgsConstructor
@Service
public class RoleService extends BaseTransactionalService {

  @NonNull final RoleRepository roleRepository;

  @NonNull final PermissionRepository permissionRepository;

  /**
   * ロールマスタを検索します。
   *
   * @param criteria
   * @param pageable
   * @return
   */
  @Transactional(readOnly = true) // 読み取りのみの場合は指定する
  public Page<Role> findAll(RoleCriteria criteria, Pageable pageable) {
    Assert.notNull(criteria, "criteria must not be null");
    return roleRepository.findAll(criteria, pageable);
  }

  /**
   * ロールマスタを1件取得します。
   *
   * @return
   */
  @Transactional(readOnly = true)
  public Optional<Role> findOne(RoleCriteria criteria) {
    Assert.notNull(criteria, "criteria must not be null");
    val role = roleRepository.findOne(criteria);

    role.ifPresent(
        r -> {
          val permissions = getPermissions();
          r.getPermissions().addAll(permissions);
        });

    return role;
  }

  /**
   * ロールマスタを1件取得します。
   *
   * @return
   */
  @Transactional(readOnly = true)
  public Role findById(final Long id) {
    Assert.notNull(id, "id must not be null");
    val role = roleRepository.findById(id);
    val permissions = getPermissions();
    role.getPermissions().addAll(permissions);
    return role;
  }

  /**
   * ロールマスタを追加します。
   *
   * @param inputRole
   * @return
   */
  public Role create(final Role inputRole) {
    Assert.notNull(inputRole, "inputRole must not be null");
    return roleRepository.create(inputRole);
  }

  /**
   * ロールマスタを更新します。
   *
   * @param inputRole
   * @return
   */
  public Role update(final Role inputRole) {
    Assert.notNull(inputRole, "inputRole must not be null");
    return roleRepository.update(inputRole);
  }

  /**
   * ロールマスタを論理削除します。
   *
   * @return
   */
  public Role delete(final Long id) {
    Assert.notNull(id, "id must not be null");
    return roleRepository.delete(id);
  }

  /**
   * ロールマスタを書き出します。
   *
   * @param outputStream
   * @param
   * @return
   */
  @Transactional(readOnly = true) // 読み取りのみの場合は指定する
  public void writeToOutputStream(OutputStream outputStream, RoleCriteria criteria, Class<?> clazz)
      throws IOException {
    Assert.notNull(criteria, "criteria must not be null");
    try (val data = roleRepository.findAll(criteria)) {
      CsvUtils.writeCsv(outputStream, clazz, data, role -> modelMapper.map(role, clazz));
    }
  }

  private List<Permission> getPermissions() {
    return permissionRepository.findAll(new PermissionCriteria(), Pageable.unpaged()).getContent();
  }
}
