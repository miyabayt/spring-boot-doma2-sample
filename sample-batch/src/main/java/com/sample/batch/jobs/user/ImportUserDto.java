package com.sample.batch.jobs.user;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import com.sample.batch.item.ItemPosition;
import com.sample.domain.validator.annotation.PhoneNumber;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ImportUserDto implements ItemPosition {

    // 名前
    @NotEmpty
    String firstName;

    // 苗字
    @NotEmpty
    String lastName;

    // メールアドレス
    @Email
    String email;

    // 電話番号
    @PhoneNumber
    String tel;

    // 郵便番号
    @NotEmpty
    String zip;

    // 住所
    @NotEmpty
    String address;

    // 取り込み元ファイル
    String sourceName;

    int position;
}
