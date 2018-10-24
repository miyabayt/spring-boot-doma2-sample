package com.sample.web.admin.controller.html.system.holidays;

import java.time.LocalDate;

import javax.validation.constraints.NotEmpty;

import com.sample.web.base.controller.html.BaseForm;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class HolidayForm extends BaseForm {

    private static final long serialVersionUID = 6646321876052100374L;

    Long id;

    // 祝日キー
    @NotEmpty
    String holidayName;

    // 日付
    LocalDate holidayDate;
}
