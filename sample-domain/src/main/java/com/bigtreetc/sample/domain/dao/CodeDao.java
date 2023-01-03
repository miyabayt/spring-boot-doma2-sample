package com.bigtreetc.sample.domain.dao;

import com.bigtreetc.sample.domain.entity.Code;
import com.bigtreetc.sample.domain.entity.CodeCriteria;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import org.seasar.doma.*;
import org.seasar.doma.boot.ConfigAutowireable;
import org.seasar.doma.jdbc.SelectOptions;

@ConfigAutowireable
@Dao
public interface CodeDao {

  /**
   * コードを取得します。
   *
   * @param criteria
   * @param options
   * @return
   */
  @Select(strategy = SelectType.COLLECT)
  <R> R selectAll(
      final CodeCriteria criteria,
      final SelectOptions options,
      final Collector<Code, ?, R> collector);

  /**
   * コードを1件取得します。
   *
   * @param id
   * @return
   */
  @Select
  Optional<Code> selectById(Long id);

  /**
   * コードを1件取得します。
   *
   * @param criteria
   * @return
   */
  @Select
  Optional<Code> select(CodeCriteria criteria);

  /**
   * コードを登録します。
   *
   * @param Code
   * @return
   */
  @Insert
  int insert(Code Code);

  /**
   * コードを更新します。
   *
   * @param code
   * @return
   */
  @Update
  int update(Code code);

  /**
   * コードを論理削除します。
   *
   * @param code
   * @return
   */
  @Update(excludeNull = true) // NULLの項目は更新対象にしない
  int delete(Code code);

  /**
   * コードを一括登録します。
   *
   * @param codes
   * @return
   */
  @BatchInsert
  int[] insert(List<Code> codes);
}
