package com.sample.domain.dto.common;

import java.util.List;

/**
 * ページファクトリ
 */
public class PageFactory {

    /**
     * インスタンスを生成して返します。
     * 
     * @param data
     * @param pageable
     * @param count
     * @param <T>
     * @return
     */
    public static <T> Page<T> create(List<T> data, Pageable pageable, long count) {
        return new PageImpl<>(data, pageable, count);
    }
}
