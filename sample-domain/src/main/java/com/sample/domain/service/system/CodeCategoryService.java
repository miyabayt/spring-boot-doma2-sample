package com.sample.domain.service.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.sample.domain.dto.common.Page;
import com.sample.domain.dto.common.Pageable;
import com.sample.domain.dto.system.CodeCategory;
import com.sample.domain.exception.NoDataFoundException;
import com.sample.domain.repository.system.CodeCategoryRepository;
import com.sample.domain.service.BaseTransactionalService;

import lombok.val;

/**
 * コード分類サービス
 */
@Service
public class CodeCategoryService extends BaseTransactionalService {

    @Autowired
    CodeCategoryRepository codeCategoryRepository;

    /**
     * コード分類を全件取得します。
     *
     * @return
     */
    @Transactional(readOnly = true) // 読み取りのみの場合は指定する
    public Page<CodeCategory> fetchAll() {
        // ページングを指定する
        val pageable = Pageable.NO_LIMIT;
        return codeCategoryRepository.findAll(new CodeCategory(), pageable);
    }

    /**
     * コード分類を一括取得します。
     *
     * @return
     */
    @Transactional(readOnly = true) // 読み取りのみの場合は指定する
    public Page<CodeCategory> findAll(CodeCategory where, Pageable pageable) {
        Assert.notNull(where, "where must not be null");
        return codeCategoryRepository.findAll(where, pageable);
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
     * @param categoryKey
     * @return
     */
    @Transactional(readOnly = true)
    public CodeCategory findByKey(final String categoryKey) {
        Assert.notNull(categoryKey, "categoryKey must not be null");

        val where = new CodeCategory();
        where.setCategoryKey(categoryKey);

        // 1件取得
        return codeCategoryRepository.findOne(where)
                .orElseThrow(() -> new NoDataFoundException("category_key=" + categoryKey + " のデータが見つかりません。"));
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
