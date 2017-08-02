package com.sample.batch.jobs.user;

import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import com.sample.batch.context.BatchContext;
import com.sample.batch.jobs.BaseItemProcessor;
import com.sample.domain.dto.User;

/**
 * ユーザー
 */
public class UserItemProcessor extends BaseItemProcessor<User, User> {

    @Override
    protected void onValidationError(BatchContext context, BindingResult result, User item) {
    }

    @Override
    protected User doProcess(BatchContext context, User user) {
        User transform = new User();
        transform.setLastName(user.getLastName());
        transform.setFirstName(user.getFirstName());
        transform.setEmail(user.getEmail());
        transform.setTel(user.getTel());
        transform.setZip(user.getZip());
        transform.setAddress(user.getAddress());
        return transform;
    }

    @Override
    protected Validator getValidator() {
        return null;
    }
}
