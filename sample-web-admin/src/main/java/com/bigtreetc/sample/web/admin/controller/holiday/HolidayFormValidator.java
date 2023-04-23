package com.bigtreetc.sample.web.admin.controller.holiday;

import com.bigtreetc.sample.domain.validator.AbstractValidator;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

/** 祝日マスタ登録 入力チェック */
@Component
public class HolidayFormValidator extends AbstractValidator<HolidayForm> {

  @Override
  protected void doValidate(HolidayForm form, Errors errors) {}
}
