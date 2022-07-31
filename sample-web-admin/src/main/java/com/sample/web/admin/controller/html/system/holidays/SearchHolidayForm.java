package com.sample.web.admin.controller.html.system.holidays;

import com.sample.web.base.controller.html.BaseSearchForm;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SearchHolidayForm extends BaseSearchForm {

  private static final long serialVersionUID = 7228669911978606034L;

  Long id;

  // 祝日名
  String holidayName;

  // 日付
  LocalDate holidayDate;
}
