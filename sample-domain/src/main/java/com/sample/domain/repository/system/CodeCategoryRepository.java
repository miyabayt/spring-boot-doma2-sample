package com.sample.domain.repository.system;

import static com.sample.domain.util.DomaUtils.createSelectOptions;
import static java.util.stream.Collectors.toList;

import com.sample.domain.dao.system.CodeCategoryDao;
import com.sample.domain.dto.common.Page;
import com.sample.domain.dto.common.Pageable;
import com.sample.domain.dto.system.CodeCategory;
import com.sample.domain.dto.system.CodeCategoryCriteria;
import com.sample.domain.exception.NoDataFoundException;
import com.sample.domain.service.BaseRepository;
import java.util.List;
import java.util.Optional;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Repository;

/** コード分類リポジトリ */
@Repository
public class CodeCategoryRepository extends BaseRepository {

  @Autowired CodeCategoryDao codeCategoryDao;

  @Autowired(required = false)
  CacheManager cacheManager;

  /**
   * コード分類を全件取得します。
   *
   * @return
   */
  public List<CodeCategory> fetchAll() {
    // ページングを指定する
    val pageable = Pageable.NO_LIMIT;
    val options = createSelectOptions(pageable).count();
    return codeCategoryDao.selectAll(new CodeCategoryCriteria(), options, toList());
  }

  /**
   * コード分類を複数取得します。
   *
   * @param criteria
   * @param pageable
   * @return
   */
  public Page<CodeCategory> findAll(CodeCategoryCriteria criteria, Pageable pageable) {
    // ページングを指定する
    val options = createSelectOptions(pageable).count();
    val data = codeCategoryDao.selectAll(criteria, options, toList());
    return pageFactory.create(data, pageable, options.getCount());
  }

  /**
   * コード分類を取得します。
   *
   * @param criteria
   * @return
   */
  public Optional<CodeCategory> findOne(CodeCategoryCriteria criteria) {
    // 1件取得
    return codeCategoryDao.select(criteria);
  }

  /**
   * コード分類を取得します。
   *
   * @return
   */
  @Cacheable(cacheNames = "code_category", key = "#id")
  public CodeCategory findById(final Long id) {
    // 1件取得
    return codeCategoryDao
        .selectById(id)
        .orElseThrow(() -> new NoDataFoundException("codeCategory_id=" + id + " のデータが見つかりません。"));
  }

  /**
   * コード分類を追加します。
   *
   * @param inputCodeCategory
   * @return
   */
  @Caching(
      put = { //
        @CachePut(cacheNames = "code_category", key = "#inputCodeCategory.id"),
        @CachePut(cacheNames = "code_category", key = "#inputCodeCategory.categoryKey") //
      })
  public CodeCategory create(final CodeCategory inputCodeCategory) {
    // 1件登録
    codeCategoryDao.insert(inputCodeCategory);
    return inputCodeCategory;
  }

  /**
   * コード分類を更新します。
   *
   * @param inputCodeCategory
   * @return
   */
  @Caching(
      put = { //
        @CachePut(cacheNames = "code_category", key = "#inputCodeCategory.id"),
        @CachePut(cacheNames = "code_category", key = "#inputCodeCategory.categoryKey") //
      })
  public CodeCategory update(final CodeCategory inputCodeCategory) {
    // 1件更新
    int updated = codeCategoryDao.update(inputCodeCategory);

    if (updated < 1) {
      throw new NoDataFoundException(
          "codeCategory_id=" + inputCodeCategory.getId() + " のデータが見つかりません。");
    }

    return inputCodeCategory;
  }

  /**
   * コード分類を論理削除します。
   *
   * @return
   */
  @Caching(
      evict = { //
        @CacheEvict(cacheNames = "code_category", key = "#inputCodeCategory.id"),
        @CacheEvict(cacheNames = "code_category", key = "#inputCodeCategory.categoryKey") //
      })
  public CodeCategory delete(final Long id) {
    val codeCategory =
        codeCategoryDao
            .selectById(id)
            .orElseThrow(
                () -> new NoDataFoundException("codeCategory_id=" + id + " のデータが見つかりません。"));

    int updated = codeCategoryDao.delete(codeCategory);

    if (updated < 1) {
      throw new NoDataFoundException("codeCategory_id=" + id + " は更新できませんでした。");
    }

    return codeCategory;
  }

  @EventListener(ApplicationReadyEvent.class)
  public void loadCache() {
    // キャッシュする
    if (cacheManager != null) {
      val cache = cacheManager.getCache("code_category");
      fetchAll()
          .forEach(
              c -> {
                cache.put(c.getCategoryKey(), c);
                cache.put(c.getId(), c);
              });
    }
  }
}
