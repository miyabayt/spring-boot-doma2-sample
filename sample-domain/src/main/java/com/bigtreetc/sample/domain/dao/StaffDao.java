package com.bigtreetc.sample.domain.dao;

import com.bigtreetc.sample.domain.entity.Staff;
import com.bigtreetc.sample.domain.entity.StaffCriteria;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Stream;
import org.seasar.doma.*;
import org.seasar.doma.boot.ConfigAutowireable;
import org.seasar.doma.jdbc.SelectOptions;
import org.seasar.doma.message.Message;

@ConfigAutowireable
@Dao
public interface StaffDao {

  /**
   * 担当者を取得します。
   *
   * @param criteria
   * @param options
   * @return
   */
  @Select(strategy = SelectType.COLLECT)
  <R> R selectAll(
      final StaffCriteria criteria,
      final SelectOptions options,
      final Collector<Staff, ?, R> collector);

  /**
   * 担当者マスタを検索します。
   *
   * @param criteria
   * @return
   */
  @Select
  @Suppress(messages = {Message.DOMA4274})
  Stream<Staff> selectAll(final StaffCriteria criteria);

  /**
   * 担当者マスタを1件取得します。
   *
   * @param id
   * @return
   */
  @Select
  Optional<Staff> selectById(Long id);

  /**
   * 担当者を1件取得します。
   *
   * @param criteria
   * @return
   */
  @Select
  Optional<Staff> select(StaffCriteria criteria);

  /**
   * 担当者を登録します。
   *
   * @param Staff
   * @return
   */
  @Insert
  int insert(Staff Staff);

  /**
   * 担当者を更新します。
   *
   * @param staff
   * @return
   */
  @Update
  int update(Staff staff);

  /**
   * 担当者を論理削除します。
   *
   * @param staff
   * @return
   */
  @Update(excludeNull = true) // NULLの項目は更新対象にしない
  int delete(Staff staff);

  /**
   * 担当者を一括登録します。
   *
   * @param staffs
   * @return
   */
  @BatchInsert
  int[] insert(List<Staff> staffs);
}
