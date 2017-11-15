package com.sample.domain.service.system;

import static java.util.stream.Collectors.toList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.sample.domain.dao.system.CodeCategoryDao;
import com.sample.domain.dto.common.ID;
import com.sample.domain.dto.common.Page;
import com.sample.domain.dto.common.Pageable;
import com.sample.domain.dto.system.CodeCategory;
import com.sample.domain.exception.NoDataFoundException;
import com.sample.domain.service.BaseTransactionalService;

import lombok.val;

/**
 * コード分類サービス
 */
@Service
public class CodeCategoryService extends BaseTransactionalService {

    @Autowired
    CodeCategoryDao codeCategoryDao;

    /**
     * コード分類を一括取得します。
     *
     * @return
     */
    @Transactional(readOnly = true) // 読み取りのみの場合は指定する
    public Page<CodeCategory> findAll(CodeCategory where, Pageable pageable) {
        Assert.notNull(where, "where must not be null");

        // ページングを指定する
        val options = createSearchOptions(pageable).count();
        val codeCategories = codeCategoryDao.selectAll(where, options, toList());

        return pageFactory.create(codeCategories, pageable, options.getCount());
    }

    /**
     * コード分類を取得します。
     *
     * @return
     */
    @Transactional(readOnly = true)
    public CodeCategory findById(final ID<CodeCategory> id) {
        // 1件取得
        val codeCategory = codeCategoryDao.selectById(id)
                .orElseThrow(() -> new NoDataFoundException("code_category_id=" + id + " のデータが見つかりません。"));
        return codeCategory;
    }

    /**
     * コード分類を取得します。
     * 
     * @param categoryKey
     * @return
     */
    @Transactional(readOnly = true)
    public CodeCategory findByKey(final String categoryKey) {
        // 1件取得
        val codeCategory = codeCategoryDao.selectByKey(categoryKey)
                .orElseThrow(() -> new NoDataFoundException("category_key=" + categoryKey + " のデータが見つかりません。"));
        return codeCategory;
    }

    /**
     * コード分類を追加します。
     *
     * @param inputCodeCategory
     * @return
     */
    public CodeCategory create(final CodeCategory inputCodeCategory) {
        Assert.notNull(inputCodeCategory, "inputCodeCategory must not be null");

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
    public CodeCategory update(final CodeCategory inputCodeCategory) {
        Assert.notNull(inputCodeCategory, "inputCodeCategory must not be null");

        // 1件更新
        int updated = codeCategoryDao.update(inputCodeCategory);

        if (updated < 1) {
            throw new NoDataFoundException("code_category_id=" + inputCodeCategory.getId() + " のデータが見つかりません。");
        }

        return inputCodeCategory;
    }

    /**
     * コード分類を論理削除します。
     *
     * @return
     */
    public CodeCategory delete(final ID<CodeCategory> id) {
        val codeCategory = codeCategoryDao.selectById(id)
                .orElseThrow(() -> new NoDataFoundException("code_category_id=" + id + " のデータが見つかりません。"));

        int updated = codeCategoryDao.delete(codeCategory);

        if (updated < 1) {
            throw new NoDataFoundException("code_category_id=" + id + " は更新できませんでした。");
        }

        return codeCategory;
    }
}
