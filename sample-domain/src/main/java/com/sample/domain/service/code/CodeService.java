package com.sample.domain.service.code;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sample.domain.dao.CodeDao;
import com.sample.domain.dto.Code;
import com.sample.domain.service.BaseTransactionalService;

/**
 * コードサービス
 */
@Service
public class CodeService extends BaseTransactionalService {

    @Autowired
    CodeDao codeDao;

    @Cacheable("code") // JCacheを有効にする
    @Transactional(readOnly = true)
    public List<Code> getCode() {
        // コード定義をすべて取得する
        return codeDao.selectAll();
    }

    @CacheEvict(cacheNames = "code", allEntries = true) // キャッシュを破棄する
    @Transactional(readOnly = true)
    public void evictAllCache() {
        // コード定義のキャッシュを削除する
    }
}
