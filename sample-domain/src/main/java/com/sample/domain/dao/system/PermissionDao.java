package com.sample.domain.dao.system;

import com.sample.domain.dto.system.Permission;
import com.sample.domain.dto.system.PermissionCriteria;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import org.seasar.doma.*;
import org.seasar.doma.boot.ConfigAutowireable;
import org.seasar.doma.jdbc.SelectOptions;

@ConfigAutowireable
@Dao
public interface PermissionDao {

  /**
   * 権限を取得します。
   *
   * @param criteria
   * @param options
   * @return
   */
  @Select(strategy = SelectType.COLLECT)
  <R> R selectAll(
      final PermissionCriteria criteria,
      final SelectOptions options,
      final Collector<Permission, ?, R> collector);

  /**
   * 権限を1件取得します。
   *
   * @param id
   * @return
   */
  @Select
  Optional<Permission> selectById(Long id);

  /**
   * 権限を1件取得します。
   *
   * @param criteria
   * @return
   */
  @Select
  Optional<Permission> select(PermissionCriteria criteria);

  /**
   * 権限を登録します。
   *
   * @param permission
   * @return
   */
  @Insert
  int insert(Permission permission);

  /**
   * 権限を更新します。
   *
   * @param permission
   * @return
   */
  @Update
  int update(Permission permission);

  /**
   * 権限を論理削除します。
   *
   * @param permission
   * @return
   */
  @Update(excludeNull = true) // NULLの項目は更新対象にしない
  int delete(Permission permission);

  /**
   * 権限を一括登録します。
   *
   * @param permissions
   * @return
   */
  @BatchInsert
  int[] insert(List<Permission> permissions);

  /**
   * 権限を一括更新します。
   *
   * @param permissions
   * @return
   */
  @BatchUpdate
  int[] update(List<Permission> permissions);
}
