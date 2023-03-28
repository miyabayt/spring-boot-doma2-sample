package com.bigtreetc.sample.web.admin.controller.staff;

import com.bigtreetc.sample.web.base.controller.html.BaseForm;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class StaffForm extends BaseForm {

  private static final long serialVersionUID = -6807767990335584883L;

  Long id;

  // 名
  @NotEmpty String firstName;

  // 姓
  @NotEmpty String lastName;

  @NotEmpty String password;

  @NotEmpty String passwordConfirm;

  // メールアドレス
  @NotEmpty @Email String email;

  // 電話番号
  @Digits(fraction = 0, integer = 10)
  String tel;
}
