package com.sample.batch.jobs.user;

import java.util.Objects;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import com.sample.domain.validator.AbstractValidator;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ImportUserValidator extends AbstractValidator<ImportUserDto> {

    @Override
    protected void doValidate(ImportUserDto user, Errors errors) {

        if (user.getPosition() == 1 && !Objects.equals(user.getFirstName(), "Wyn")) {
            errors.rejectValue("firstName", "importUserJob.invalidFirstName");
        }
    }
}
