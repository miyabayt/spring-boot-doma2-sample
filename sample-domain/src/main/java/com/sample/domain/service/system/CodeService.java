package com.sample.domain.service.system;

import static java.util.stream.Collectors.toList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.sample.domain.dao.system.CodeDao;
import com.sample.domain.dto.common.ID;
import com.sample.domain.dto.common.Page;
import com.sample.domain.dto.common.Pageable;
import com.sample.domain.dto.system.Code;
import com.sample.domain.exception.NoDataFoundException;
import com.sample.domain.service.BaseTransactionalService;

import lombok.val;

/**
 * コードサービス
 */
@Service
public class CodeService extends BaseTransactionalService {

    @Autowired
    CodeDao codeDao;

    /**
     * コードを一括取得します。
     *
     * @return
     */
    @Transactional(readOnly = true) // 読み取りのみの場合は指定する
    public Page<Code> findAll(Code where, Pageable pageable) {
        Assert.notNull(where, "where must not be null");

        // ページングを指定する
        val options = createSearchOptions(pageable).count();
        val codes = codeDao.selectAll(where, options, toList());

        return pageFactory.create(codes, pageable, options.getCount());
    }

    /**
     * コードを取得します。
     *
     * @return
     */
    @Transactional(readOnly = true)
    public Code findById(final ID<Code> id) {
        // 1件取得
        val code = codeDao.selectById(id)
                .orElseThrow(() -> new NoDataFoundException("code_id=" + id + " のデータが見つかりません。"));
        return code;
    }

    /**
     * コードを取得します。
     * 
     * @param codeKey
     * @return
     */
    @Transactional(readOnly = true)
    public Code findByKey(final String codeKey) {
        // 1件取得
        val code = codeDao.selectByKey(codeKey)
                .orElseThrow(() -> new NoDataFoundException("code_key=" + codeKey + " のデータが見つかりません。"));
        return code;
    }

    /**
     * コードを追加します。
     *
     * @param inputCode
     * @return
     */
    public Code create(final Code inputCode) {
        Assert.notNull(inputCode, "inputCode must not be null");

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
        Assert.notNull(inputCode, "inputCode must not be null");

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
    public Code delete(final ID<Code> id) {
        val code = codeDao.selectById(id)
                .orElseThrow(() -> new NoDataFoundException("code_id=" + id + " のデータが見つかりません。"));

        int updated = codeDao.delete(code);

        if (updated < 1) {
            throw new NoDataFoundException("code_id=" + id + " は更新できませんでした。");
        }

        return code;
    }
}
