package com.sample.domain.service.system;

import com.sample.domain.dto.system.CodeCategory;
import com.sample.domain.dto.system.CodeCategoryCriteria;
import com.sample.domain.exception.NoDataFoundException;
import com.sample.domain.repository.system.CodeCategoryRepository;
import com.sample.domain.service.BaseTransactionalService;
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

/** コード分類サービス */
@RequiredArgsConstructor
@Service
public class CodeCategoryService extends BaseTransactionalService {

  @NonNull final CodeCategoryRepository codeCategoryRepository;

  /**
   * コード分類を全件取得します。
   *
   * @return
   */
  @Transactional(readOnly = true) // 読み取りのみの場合は指定する
  public List<CodeCategory> fetchAll() {
    return codeCategoryRepository.fetchAll();
  }

  /**
   * コード分類を複数取得します。
   *
   * @param criteria
   * @param pageable
   * @return
   */
  @Transactional(readOnly = true) // 読み取りのみの場合は指定する
  public Page<CodeCategory> findAll(CodeCategoryCriteria criteria, Pageable pageable) {
    Assert.notNull(criteria, "criteria must not be null");
    return codeCategoryRepository.findAll(criteria, pageable);
  }

  /**
   * コード分類を取得します。
   *
   * @return
   */
  @Transactional(readOnly = true)
  public Optional<CodeCategory> findOne(CodeCategoryCriteria criteria) {
    Assert.notNull(criteria, "criteria must not be null");
    return codeCategoryRepository.findOne(criteria);
  }

  /**
   * コード分類を取得します。
   *
   * @return
   */
  @Transactional(readOnly = true)
  public CodeCategory findById(final Long id) {
    Assert.notNull(id, "id must not be null");
    return codeCategoryRepository.findById(id);
  }

  /**
   * コード分類を取得します。
   *
   * @param categoryCode
   * @return
   */
  @Transactional(readOnly = true)
  public CodeCategory findByCategoryCode(final String categoryCode) {
    Assert.notNull(categoryCode, "categoryCode must not be null");

    val criteria = new CodeCategoryCriteria();
    criteria.setCategoryCode(categoryCode);

    return codeCategoryRepository
        .findOne(criteria)
        .orElseThrow(
            () -> new NoDataFoundException("category_code=" + categoryCode + " のデータが見つかりません。"));
  }

  /**
   * コード分類を追加します。
   *
   * @param inputCodeCategory
   * @return
   */
  public CodeCategory create(final CodeCategory inputCodeCategory) {
    Assert.notNull(inputCodeCategory, "inputCodeCategory must not be null");
    return codeCategoryRepository.create(inputCodeCategory);
  }

  /**
   * コード分類を更新します。
   *
   * @param inputCodeCategory
   * @return
   */
  public CodeCategory update(final CodeCategory inputCodeCategory) {
    Assert.notNull(inputCodeCategory, "inputCodeCategory must not be null");
    return codeCategoryRepository.update(inputCodeCategory);
  }

  /**
   * コード分類を論理削除します。
   *
   * @return
   */
  public CodeCategory delete(final Long id) {
    Assert.notNull(id, "id must not be null");
    return codeCategoryRepository.delete(id);
  }
}
