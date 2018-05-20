package com.sample.domain.repository.system;

import static com.sample.domain.util.DomaUtils.createSelectOptions;
import static java.util.stream.Collectors.toList;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sample.domain.dao.system.CodeDao;
import com.sample.domain.dto.common.Page;
import com.sample.domain.dto.common.Pageable;
import com.sample.domain.dto.system.Code;
import com.sample.domain.exception.NoDataFoundException;
import com.sample.domain.service.BaseRepository;

import lombok.val;

/**
 * コードリポジトリ
 */
@Repository
public class CodeRepository extends BaseRepository {

    @Autowired
    CodeDao codeDao;

    /**
     * コードを一括取得します。
     *
     * @param where
     * @param pageable
     * @return
     */
    public Page<Code> findAll(Code where, Pageable pageable) {
        // ページングを指定する
        val options = createSelectOptions(pageable).count();
        val data = codeDao.selectAll(where, options, toList());
        return pageFactory.create(data, pageable, options.getCount());
    }

    /**
     * コードを取得します。
     *
     * @param where
     * @return
     */
    public Optional<Code> findOne(Code where) {
        // 1件取得
        return codeDao.select(where);
    }

    /**
     * コードを取得します。
     *
     * @return
     */
    public Code findById(final Long id) {
        // 1件取得
        return codeDao.selectById(id).orElseThrow(() -> new NoDataFoundException("code_id=" + id + " のデータが見つかりません。"));
    }

    /**
     * コードを追加します。
     *
     * @param inputCode
     * @return
     */
    public Code create(final Code inputCode) {
        // 1件登録
        codeDao.insert(inputCode);

        return inputCode;
    }

    /**
     * コードを更新します。
     *
     * @param inputCode
     * @return
     */
    public Code update(final Code inputCode) {
        // 1件更新
        int updated = codeDao.update(inputCode);

        if (updated < 1) {
            throw new NoDataFoundException("code_id=" + inputCode.getId() + " のデータが見つかりません。");
        }

        return inputCode;
    }

    /**
     * コードを論理削除します。
     *
     * @return
     */
    public Code delete(final Long id) {
        val code = codeDao.selectById(id)
                .orElseThrow(() -> new NoDataFoundException("code_id=" + id + " のデータが見つかりません。"));

        int updated = codeDao.delete(code);

        if (updated < 1) {
            throw new NoDataFoundException("code_id=" + id + " は更新できませんでした。");
        }

        return code;
    }
}
