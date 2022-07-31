package com.sample.domain.dao.system;

import com.sample.domain.dto.system.Role;
import com.sample.domain.dto.system.RoleCriteria;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import org.seasar.doma.*;
import org.seasar.doma.boot.ConfigAutowireable;
import org.seasar.doma.jdbc.SelectOptions;

@ConfigAutowireable
@Dao
public interface RoleDao {

  /**
   * ロールを取得します。
   *
   * @param criteria
   * @param options
   * @return
   */
  @Select(strategy = SelectType.COLLECT)
  <R> R selectAll(
      final RoleCriteria criteria,
      final SelectOptions options,
      final Collector<Role, ?, R> collector);

  /**
   * ロールを1件取得します。
   *
   * @param id
   * @return
   */
  @Select
  Optional<Role> selectById(Long id);

  /**
   * ロールを1件取得します。
   *
   * @param criteria
   * @return
   */
  @Select
  Optional<Role> select(RoleCriteria criteria);

  /**
   * ロールを登録します。
   *
   * @param role
   * @return
   */
  @Insert
  int insert(Role role);

  /**
   * ロールを更新します。
   *
   * @param role
   * @return
   */
  @Update
  int update(Role role);

  /**
   * ロールを論理削除します。
   *
   * @param role
   * @return
   */
  @Update(excludeNull = true) // NULLの項目は更新対象にしない
  int delete(Role role);

  /**
   * ロールを一括登録します。
   *
   * @param roles
   * @return
   */
  @BatchInsert
  int[] insert(List<Role> roles);

  /**
   * ロールを一括更新します。
   *
   * @param roles
   * @return
   */
  @BatchUpdate
  int[] update(List<Role> roles);
}
