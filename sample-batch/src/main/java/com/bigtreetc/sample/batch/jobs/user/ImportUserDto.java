package com.bigtreetc.sample.batch.jobs.user;

import com.bigtreetc.sample.batch.item.ItemPosition;
import com.bigtreetc.sample.domain.validator.annotation.PhoneNumber;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ImportUserDto implements ItemPosition {

  // 名
  @NotEmpty String firstName;

  // 姓
  @NotEmpty String lastName;

  // メールアドレス
  @Email String email;

  // 電話番号
  @PhoneNumber String tel;

  // 郵便番号
  @NotEmpty String zip;

  // 住所
  @NotEmpty String address;

  // 取込元ファイル
  String sourceName;

  int position;
}
