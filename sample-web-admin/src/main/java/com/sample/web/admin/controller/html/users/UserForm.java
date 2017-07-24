package com.sample.web.admin.controller.html.users;

import javax.validation.constraints.Digits;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import com.sample.web.base.controller.html.BaseForm;
import com.sample.web.base.validator.annotation.ContentType;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserForm extends BaseForm {

    private static final long serialVersionUID = -6807767990335584883L;

    Integer id;

    // 名前
    @NotEmpty
    String firstName;

    // 苗字
    @NotEmpty
    String lastName;

    @NotEmpty
    String password;

    @NotEmpty
    String passwordConfirm;

    // メールアドレス
    @NotEmpty
    @Email
    String email;

    // 電話番号
    @Digits(fraction = 0, integer = 10)
    String tel;

    // 郵便番号
    String zip;

    // 住所
    String address;

    // 添付ファイル
    @ContentType(allowed = { MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_GIF_VALUE })
    transient MultipartFile userImage; // serializableではないのでtransientにする
}
