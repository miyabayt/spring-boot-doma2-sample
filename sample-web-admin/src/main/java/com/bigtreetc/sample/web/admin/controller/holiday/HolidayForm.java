package com.bigtreetc.sample.web.admin.controller.holiday;

import com.bigtreetc.sample.web.base.controller.html.BaseForm;
import java.time.LocalDate;
import javax.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class HolidayForm extends BaseForm {

  private static final long serialVersionUID = 6646321876052100374L;

  Long id;

  // 名称
  @NotEmpty String holidayName;

  // 日付
  LocalDate holidayDate;
}
