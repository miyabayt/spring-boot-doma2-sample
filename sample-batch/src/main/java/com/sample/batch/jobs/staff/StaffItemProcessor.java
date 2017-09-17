package com.sample.batch.jobs.staff;

import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import com.sample.batch.context.BatchContext;
import com.sample.batch.jobs.BaseItemProcessor;
import com.sample.domain.dto.system.Staff;

/**
 * 担当者
 */
public class StaffItemProcessor extends BaseItemProcessor<Staff, Staff> {

    @Override
    protected void onValidationError(BatchContext context, BindingResult result, Staff item) {
    }

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
