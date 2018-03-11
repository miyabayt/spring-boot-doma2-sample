package com.sample.web.admin.controller.html.system.holidays;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import com.sample.domain.validator.AbstractValidator;

/**
 * 祝日登録 入力チェック
 */
@Component
public class HolidayFormValidator extends AbstractValidator<HolidayForm> {

    @Override
    protected void doValidate(HolidayForm form, Errors errors) {

    }
}
