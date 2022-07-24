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
   * 役割を取得します。
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
   * 役割を1件取得します。
   *
   * @param id
   * @return
   */
  @Select
  Optional<Role> selectById(Long id);

  /**
   * 役割を1件取得します。
   *
   * @param criteria
   * @return
   */
  @Select
  Optional<Role> select(RoleCriteria criteria);

  /**
   * 役割を登録します。
   *
   * @param role
   * @return
   */
  @Insert
  int insert(Role role);

  /**
   * 役割を更新します。
   *
   * @param role
   * @return
   */
  @Update
  int update(Role role);

  /**
   * 役割を論理削除します。
   *
   * @param role
   * @return
   */
  @Update(excludeNull = true) // NULLの項目は更新対象にしない
  int delete(Role role);

  /**
   * 役割を一括登録します。
   *
   * @param roles
   * @return
   */
  @BatchInsert
  int[] insert(List<Role> roles);

  /**
   * 役割を一括更新します。
   *
   * @param roles
   * @return
   */
  @BatchUpdate
  int[] update(List<Role> roles);
}
