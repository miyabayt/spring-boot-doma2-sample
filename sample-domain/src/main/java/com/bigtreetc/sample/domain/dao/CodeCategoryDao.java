package com.bigtreetc.sample.domain.dao;

import com.bigtreetc.sample.domain.entity.CodeCategory;
import com.bigtreetc.sample.domain.entity.CodeCategoryCriteria;
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
public interface CodeCategoryDao {

  /**
   * コード分類マスタを取得します。
   *
   * @param criteria
   * @param options
   * @return
   */
  @Select(strategy = SelectType.COLLECT)
  <R> R selectAll(
      final CodeCategoryCriteria criteria,
      final SelectOptions options,
      final Collector<CodeCategory, ?, R> collector);

  /**
   * コード分類マスタを検索します。
   *
   * @param criteria
   * @return
   */
  @Select
  @Suppress(messages = {Message.DOMA4274})
  Stream<CodeCategory> selectAll(final CodeCategoryCriteria criteria);

  /**
   * コード分類マスタを1件取得します。
   *
   * @param id
   * @return
   */
  @Select
  Optional<CodeCategory> selectById(Long id);

  /**
   * コード分類マスタを1件取得します。
   *
   * @param criteria
   * @return
   */
  @Select
  Optional<CodeCategory> select(CodeCategoryCriteria criteria);

  /**
   * コード分類マスタを登録します。
   *
   * @param CodeCategory
   * @return
   */
  @Insert
  int insert(CodeCategory CodeCategory);

  /**
   * コード分類マスタを更新します。
   *
   * @param codeCategory
   * @return
   */
  @Update
  int update(CodeCategory codeCategory);

  /**
   * コード分類マスタを論理削除します。
   *
   * @param codeCategory
   * @return
   */
  @Update(excludeNull = true) // NULLの項目は更新対象にしない
  int delete(CodeCategory codeCategory);

  /**
   * コード分類マスタを一括登録します。
   *
   * @param codes
   * @return
   */
  @BatchInsert
  int[] insert(List<CodeCategory> codes);
}
