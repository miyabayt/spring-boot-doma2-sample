package com.sample.domain.dto.common;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DefaultPageable implements Pageable {

    int page = 1;

    int perpage = 10;

    public DefaultPageable(int page, int perpage) {
        this.page = page;
        this.perpage = perpage;
    }
}
