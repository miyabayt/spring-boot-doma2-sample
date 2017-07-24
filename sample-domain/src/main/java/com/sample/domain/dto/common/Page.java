package com.sample.domain.dto.common;

import java.util.List;

/**
 * ページネーションを利用している場合に、ページ数や現在のページ数を保持する
 * 
 * @param <T>
 */
public interface Page<T> extends Pageable {

    /**
     * データを返します。
     * 
     * @return
     */
    List<T> getData();

    /**
     * データ件数を返します。
     * 
     * @return
     */
    long getCount();

    /**
     * ページ数を返します。
     *
     * @return
     */
    int getTotalPages();
}
