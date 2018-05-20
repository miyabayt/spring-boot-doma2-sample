package com.sample.domain.util;

import org.seasar.doma.jdbc.SelectOptions;

import com.sample.domain.dto.common.Pageable;

/**
 * Doma関連ユーティリティ
 */
public class DomaUtils {

    /**
     * SearchOptionsを作成して返します。
     *
     * @return
     */
    public static SelectOptions createSelectOptions() {
        return SelectOptions.get();
    }

    /**
     * SearchOptionsを作成して返します。
     *
     * @param pageable
     * @return
     */
    public static SelectOptions createSelectOptions(Pageable pageable) {
        int page = pageable.getPage();
        int perpage = pageable.getPerpage();
        return createSelectOptions(page, perpage);
    }

    /**
     * SearchOptionsを作成して返します。
     *
     * @param page
     * @param perpage
     * @return
     */
    public static SelectOptions createSelectOptions(int page, int perpage) {
        int offset = (page - 1) * perpage;
        return SelectOptions.get().offset(offset).limit(perpage);
    }
}
