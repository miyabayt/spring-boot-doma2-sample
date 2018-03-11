package com.sample.web.admin.controller.html.system.roles;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import com.sample.domain.validator.AbstractValidator;

/**
 * 役割登録 入力チェック
 */
@Component
public class RoleFormValidator extends AbstractValidator<RoleForm> {

    @Override
    protected void doValidate(RoleForm form, Errors errors) {

    }
}
