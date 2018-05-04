package com.sample.web.admin.controller.html.system.holidays;

import java.time.LocalDate;

import com.sample.domain.dto.common.Pageable;
import com.sample.web.base.controller.html.BaseSearchForm;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SearchHolidayForm extends BaseSearchForm implements Pageable {

    private static final long serialVersionUID = 7228669911978606034L;

    Long id;

    // 祝日名
    String holidayName;

    // 日付
    LocalDate holidayDate;
}
