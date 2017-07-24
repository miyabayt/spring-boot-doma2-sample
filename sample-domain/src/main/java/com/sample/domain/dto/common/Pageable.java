package com.sample.domain.dto.common;

/**
 * ページング可能であることを示す
 */
public interface Pageable {

    /**
     * 
     * @return
     */
    int getPage();

    /**
     * 
     * @return
     */
    int getPerpage();
}
