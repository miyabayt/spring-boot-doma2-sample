package com.sample.domain.dao.system;

import com.sample.domain.dto.system.CodeCategory;
import com.sample.domain.dto.system.CodeCategoryCriteria;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import org.seasar.doma.*;
import org.seasar.doma.boot.ConfigAutowireable;
import org.seasar.doma.jdbc.SelectOptions;

@ConfigAutowireable
@Dao
public interface CodeCategoryDao {

  /**
   * コード分類定義を取得します。
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
   * コード分類定義を全件取得します。
   *
   * @return
   */
  @Select
  List<CodeCategory> fetchAll();

  /**
   * コード分類を1件取得します。
   *
   * @param id
   * @return
   */
  @Select
  Optional<CodeCategory> selectById(Long id);

  /**
   * コード分類を1件取得します。
   *
   * @param categoryKey
   * @return
   */
  @Select
  Optional<CodeCategory> selectByKey(String categoryKey);

  /**
   * コード分類を1件取得します。
   *
   * @param criteria
   * @return
   */
  @Select
  Optional<CodeCategory> select(CodeCategoryCriteria criteria);

  /**
   * コード分類を登録します。
   *
   * @param CodeCategory
   * @return
   */
  @Insert
  int insert(CodeCategory CodeCategory);

  /**
   * コード分類を更新します。
   *
   * @param codeCategory
   * @return
   */
  @Update(exclude = {"categoryKey"})
  int update(CodeCategory codeCategory);

  /**
   * コード分類を論理削除します。
   *
   * @param codeCategory
   * @return
   */
  @Update(excludeNull = true) // NULLの項目は更新対象にしない
  int delete(CodeCategory codeCategory);

  /**
   * コード分類を一括登録します。
   *
   * @param codes
   * @return
   */
  @BatchInsert
  int[] insert(List<CodeCategory> codes);
}
