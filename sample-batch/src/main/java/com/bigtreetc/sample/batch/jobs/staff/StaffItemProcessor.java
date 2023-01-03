package com.bigtreetc.sample.batch.jobs.staff;

import com.bigtreetc.sample.batch.context.BatchContext;
import com.bigtreetc.sample.batch.jobs.BaseItemProcessor;
import com.bigtreetc.sample.domain.entity.Staff;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

/** 担当者 */
public class StaffItemProcessor extends BaseItemProcessor<Staff, Staff> {

  @Override
  protected void onValidationError(BatchContext context, BindingResult result, Staff item) {}

  @Override
  protected Staff doProcess(BatchContext context, Staff staff) {
    Staff transform = new Staff();
    transform.setLastName(staff.getLastName());
    transform.setFirstName(staff.getFirstName());
    transform.setEmail(staff.getEmail());
    transform.setTel(staff.getTel());
    return transform;
  }

  @Override
  protected Validator getValidator() {
    return null;
  }
}
