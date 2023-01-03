package com.bigtreetc.sample.batch.jobs.user;

import com.bigtreetc.sample.domain.validator.AbstractValidator;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

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
