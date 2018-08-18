package com.sample.web.admin.controller.html.system.codecategories;

import com.sample.domain.dto.common.Pageable;
import com.sample.web.base.controller.html.BaseSearchForm;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SearchCodeCategoryForm extends BaseSearchForm implements Pageable {

    private static final long serialVersionUID = -4384562527048697811L;

    Long id;

    String categoryKey;

    String categoryName;
}
