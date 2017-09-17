package com.sample.batch.jobs.birthdayMail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import com.sample.batch.context.BatchContext;
import com.sample.batch.jobs.BaseItemProcessor;
import com.sample.domain.dto.common.CommaSeparatedString;
import com.sample.domain.dto.system.SendMailQueue;
import com.sample.domain.dto.user.User;
import com.sample.domain.helper.SendMailHelper;

import lombok.val;

public class BirthdayMailProcessor extends BaseItemProcessor<User, SendMailQueue> {

    @Value("${spring.mail.properties.mail.from}")
    String from;

    private static final String SUBJECT = "誕生日おめでとう！";

    @Autowired
    SendMailHelper sendMailHelper;

    @Override
    protected void onValidationError(BatchContext context, BindingResult result, User item) {
    }

    @Override
    protected SendMailQueue doProcess(BatchContext context, User user) {
        val body = sendMailHelper.getMailBody("birthdayMail", "user", user);
        val to = CommaSeparatedString.of(user.getEmail());

        val transform = new SendMailQueue();
        transform.setFrom(from);
        transform.setSubject(SUBJECT);
        transform.setBody(body);
        transform.setTo(to);

        return transform;
    }

    @Override
    protected Validator getValidator() {
        return null;
    }
}
