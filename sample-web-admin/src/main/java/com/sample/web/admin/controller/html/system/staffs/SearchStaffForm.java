package com.sample.web.admin.controller.html.system.staffs;

import com.sample.domain.dto.common.Pageable;
import com.sample.web.base.controller.html.BaseSearchForm;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SearchStaffForm extends BaseSearchForm implements Pageable {

    private static final long serialVersionUID = 4131372368553937515L;

    Long id;

    String firstName;

    String lastName;

    String email;
}
