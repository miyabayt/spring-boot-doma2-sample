package com.bigtreetc.sample.domain.dao;

import com.bigtreetc.sample.domain.entity.Holiday;
import com.bigtreetc.sample.domain.entity.HolidayCriteria;
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
public interface HolidayDao {

  /**
   * 祝日マスタを検索します。
   *
   * @param criteria
   * @param options
   * @param collector
   * @return
   */
  @Select(strategy = SelectType.COLLECT)
  <R> R selectAll(
      final HolidayCriteria criteria,
      final SelectOptions options,
      final Collector<Holiday, ?, R> collector);

  /**
   * 祝日マスタを検索します。
   *
   * @param criteria
   * @return
   */
  @Select
  @Suppress(messages = {Message.DOMA4274})
  Stream<Holiday> selectAll(final HolidayCriteria criteria);

  /**
   * 祝日マスタを1件取得します。
   *
   * @param id
   * @return
   */
  @Select
  Optional<Holiday> selectById(Long id);

  /**
   * 祝日マスタを1件取得します。
   *
   * @param criteria
   * @return
   */
  @Select
  Optional<Holiday> select(HolidayCriteria criteria);

  /**
   * 祝日マスタを登録します。
   *
   * @param holiday
   * @return
   */
  @Insert
  int insert(Holiday holiday);

  /**
   * 祝日マスタを更新します。
   *
   * @param holiday
   * @return
   */
  @Update
  int update(Holiday holiday);

  /**
   * 祝日マスタを論理削除します。
   *
   * @param holiday
   * @return
   */
  @Update(excludeNull = true) // NULLの項目は更新対象にしない
  int delete(Holiday holiday);

  /**
   * 祝日マスタを一括登録します。
   *
   * @param holidays
   * @return
   */
  @BatchInsert
  int[] insert(List<Holiday> holidays);

  /**
   * 祝日マスタを一括更新します。
   *
   * @param holidays
   * @return
   */
  @BatchUpdate
  int[] update(List<Holiday> holidays);
}
