package com.sample.domain.repository.system;

import static com.sample.domain.util.DomaUtils.createSelectOptions;
import static java.util.stream.Collectors.toList;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sample.domain.dao.system.CodeCategoryDao;
import com.sample.domain.dto.common.Page;
import com.sample.domain.dto.common.Pageable;
import com.sample.domain.dto.system.CodeCategory;
import com.sample.domain.exception.NoDataFoundException;
import com.sample.domain.service.BaseRepository;

import lombok.val;

/**
 * コード分類リポジトリ
 */
@Repository
public class CodeCategoryRepository extends BaseRepository {

    @Autowired
    CodeCategoryDao codeCategoryDao;

    /**
     * コード分類を一括取得します。
     *
     * @param where
     * @param pageable
     * @return
     */
    public Page<CodeCategory> findAll(CodeCategory where, Pageable pageable) {
        // ページングを指定する
        val options = createSelectOptions(pageable).count();
        val data = codeCategoryDao.selectAll(where, options, toList());
        return pageFactory.create(data, pageable, options.getCount());
    }

    /**
     * コード分類を取得します。
     *
     * @param where
     * @return
     */
    public Optional<CodeCategory> findOne(CodeCategory where) {
        // 1件取得
        return codeCategoryDao.select(where);
    }

    /**
     * コード分類を取得します。
     *
     * @return
     */
    public CodeCategory findById(final Long id) {
        // 1件取得
        return codeCategoryDao.selectById(id)
                .orElseThrow(() -> new NoDataFoundException("codeCategory_id=" + id + " のデータが見つかりません。"));
    }

    /**
     * コード分類を追加します。
     *
     * @param inputCodeCategory
     * @return
     */
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
    public CodeCategory update(final CodeCategory inputCodeCategory) {
        // 1件更新
        int updated = codeCategoryDao.update(inputCodeCategory);

        if (updated < 1) {
            throw new NoDataFoundException("codeCategory_id=" + inputCodeCategory.getId() + " のデータが見つかりません。");
        }

        return inputCodeCategory;
    }

    /**
     * コード分類を論理削除します。
     *
     * @return
     */
    public CodeCategory delete(final Long id) {
        val codeCategory = codeCategoryDao.selectById(id)
                .orElseThrow(() -> new NoDataFoundException("codeCategory_id=" + id + " のデータが見つかりません。"));

        int updated = codeCategoryDao.delete(codeCategory);

        if (updated < 1) {
            throw new NoDataFoundException("codeCategory_id=" + id + " は更新できませんでした。");
        }

        return codeCategory;
    }
}
