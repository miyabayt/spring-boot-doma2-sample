package com.sample.domain.repository.system;

import static com.sample.domain.util.DomaUtils.createSelectOptions;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
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

    @Autowired
    CacheManager cacheManager;

    /**
     * コードを全件取得します。
     *
     * @return
     */
    @Cacheable(cacheNames = "code", key = "#root.method")
    public List<Code> fetchAll() {
        // ページングを指定する
        val pageable = Pageable.NO_LIMIT;
        val options = createSelectOptions(pageable).count();

        // キャッシュする
        val cache = cacheManager.getCache("code");
        val list = codeDao.selectAll(new Code(), options, toList());
        list.forEach(c -> {
            cache.put(c.getCategoryKey() + "_" + c.getCodeKey(), c);
            cache.put(c.getId(), c);
        });

        return list;
    }

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
    @Cacheable(cacheNames = "code", key = "#id")
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
    @Caching(put = { //
            @CachePut(cacheNames = "code_category", key = "#inputCode.id"),
            @CachePut(cacheNames = "code_category", key = "#inputCode.categoryKey + '_' + #inputCode.codeKey") //
    })
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
    @Caching(put = { //
            @CachePut(cacheNames = "code_category", key = "#inputCode.id"),
            @CachePut(cacheNames = "code_category", key = "#inputCode.categoryKey + '_' + #inputCode.codeKey") //
    })
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
    @Caching(evict = { //
            @CacheEvict(cacheNames = "code_category", key = "#id"),
            @CacheEvict(cacheNames = "code_category", key = "#categoryKey + '_' + #codeKey") //
    })
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
