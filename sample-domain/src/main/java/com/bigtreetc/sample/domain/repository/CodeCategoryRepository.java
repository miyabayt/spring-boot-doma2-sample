package com.bigtreetc.sample.domain.repository;

import static com.bigtreetc.sample.domain.util.DomaUtils.createSelectOptions;
import static java.util.stream.Collectors.toList;

import com.bigtreetc.sample.domain.dao.CodeCategoryDao;
import com.bigtreetc.sample.domain.entity.CodeCategory;
import com.bigtreetc.sample.domain.entity.CodeCategoryCriteria;
import com.bigtreetc.sample.domain.exception.NoDataFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

/** コード分類マスタリポジトリ */
@RequiredArgsConstructor
@Repository
public class CodeCategoryRepository {

  @NonNull final CodeCategoryDao codeCategoryDao;

  /**
   * コード分類マスタを全件取得します。
   *
   * @return
   */
  public List<CodeCategory> fetchAll() {
    val pageable = Pageable.unpaged();
    val options = createSelectOptions(pageable).count();
    return codeCategoryDao.selectAll(new CodeCategoryCriteria(), options, toList());
  }

  /**
   * コード分類マスタを検索します。
   *
   * @param criteria
   * @param pageable
   * @return
   */
  public Page<CodeCategory> findAll(CodeCategoryCriteria criteria, Pageable pageable) {
    val options = createSelectOptions(pageable).count();
    val data = codeCategoryDao.selectAll(criteria, options, toList());
    return new PageImpl<>(data, pageable, options.getCount());
  }

  /**
   * コード分類マスタを検索します。
   *
   * @param criteria
   * @return
   */
  public Stream<CodeCategory> findAll(CodeCategoryCriteria criteria) {
    return codeCategoryDao.selectAll(criteria);
  }

  /**
   * コード分類マスタを取得します。
   *
   * @param criteria
   * @return
   */
  public Optional<CodeCategory> findOne(CodeCategoryCriteria criteria) {
    return codeCategoryDao.select(criteria);
  }

  /**
   * コード分類マスタを取得します。
   *
   * @return
   */
  public CodeCategory findById(final Long id) {
    return codeCategoryDao
        .selectById(id)
        .orElseThrow(() -> new NoDataFoundException("codeCategory_id=" + id + " のデータが見つかりません。"));
  }

  /**
   * コード分類マスタを登録します。
   *
   * @param inputCodeCategory
   * @return
   */
  public CodeCategory create(final CodeCategory inputCodeCategory) {
    codeCategoryDao.insert(inputCodeCategory);
    return inputCodeCategory;
  }

  /**
   * コード分類マスタを更新します。
   *
   * @param inputCodeCategory
   * @return
   */
  public CodeCategory update(final CodeCategory inputCodeCategory) {
    int updated = codeCategoryDao.update(inputCodeCategory);

    if (updated < 1) {
      throw new NoDataFoundException(
          "code_category_id=" + inputCodeCategory.getId() + " のデータが見つかりません。");
    }

    return inputCodeCategory;
  }

  /**
   * コード分類マスタを論理削除します。
   *
   * @return
   */
  public CodeCategory delete(final Long id) {
    val codeCategory =
        codeCategoryDao
            .selectById(id)
            .orElseThrow(
                () -> new NoDataFoundException("code_category_id=" + id + " のデータが見つかりません。"));

    int updated = codeCategoryDao.delete(codeCategory);

    if (updated < 1) {
      throw new NoDataFoundException("code_category_id=" + id + " は更新できませんでした。");
    }

    return codeCategory;
  }
}
