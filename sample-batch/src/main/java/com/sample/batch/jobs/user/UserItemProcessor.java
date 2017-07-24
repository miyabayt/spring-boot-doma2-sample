package com.sample.batch.jobs.user;

import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import com.sample.batch.jobs.BaseItemProcessor;
import com.sample.domain.dto.User;

/**
 * ユーザー
 */
public class UserItemProcessor extends BaseItemProcessor<User, User> {

    @Override
    protected void onValidationError(BindingResult result, User item) {
    }

    @Override
    protected User doProcess(User user) {
        User transform = new User();
        transform.setFirstName(user.getFirstName());
        transform.setLastName(user.getLastName());
        transform.setEmail(user.getEmail());
        return transform;
    }

    @Override
    protected Validator getValidator() {
        return null;
    }
}
