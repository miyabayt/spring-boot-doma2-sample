package com.sample.web.admin.controller.html.system.codes;

import com.sample.domain.dto.common.Pageable;
import com.sample.web.base.controller.html.BaseSearchForm;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SearchCodeForm extends BaseSearchForm implements Pageable {

    private static final long serialVersionUID = 223278986587249949L;

    Long id;

    String categoryKey;

    String codeKey;

    String codeValue;
}
