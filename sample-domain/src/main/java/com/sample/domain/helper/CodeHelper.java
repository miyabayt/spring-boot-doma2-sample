package com.sample.domain.helper;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sample.domain.dto.system.Code;
import com.sample.domain.dto.system.CodeCategory;
import com.sample.domain.exception.NoDataFoundException;
import com.sample.domain.repository.system.CodeCategoryRepository;
import com.sample.domain.repository.system.CodeRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * コードヘルパー
 */
@Component
@Slf4j
public class CodeHelper {

    @Autowired
    CodeCategoryRepository codeCategoryRepository;

    @Autowired
    CodeRepository codeRepository;

    /**
     * コード分類を全件取得します。
     *
     * @return
     */
    public List<CodeCategory> getCodeCategories() {
        return codeCategoryRepository.fetchAll();
    }

    /**
     * コードキーを指定してコード値を取得します。
     *
     * @param categoryKey
     * @param codeKey
     * @return
     */
    public Code getCode(String categoryKey, String codeKey) {
        return codeRepository.findByCodeKey(categoryKey, codeKey)
                .orElseThrow(() -> new NoDataFoundException("codeKey=" + codeKey + "のデータが見つかりません。"));
    }
}
