package com.sample.domain.service;

import org.seasar.doma.jdbc.SelectOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.sample.domain.dto.common.PageFactory;
import com.sample.domain.dto.common.Pageable;

@Transactional(rollbackFor = Throwable.class)
public abstract class BaseTransactionalService extends BaseService {

    @Autowired
    protected PageFactory pageFactory;

    /**
     * SearchOptionsを作成して返します。
     *
     * @param pageable
     * @return
     */
    protected SelectOptions createSearchOptions(Pageable pageable) {
        int page = pageable.getPage();
        int perpage = pageable.getPerpage();
        return createSearchOptions(page, perpage);
    }

    /**
     * SearchOptionsを作成して返します。
     *
     * @param page
     * @param perpage
     * @return
     */
    protected SelectOptions createSearchOptions(int page, int perpage) {
        int offset = (page - 1) * perpage;
        return SelectOptions.get().offset(offset).limit(perpage);
    }
}
