package com.sample.domain.dto.user;

import javax.validation.constraints.Digits;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.seasar.doma.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sample.domain.dto.common.DomaDtoImpl;
import com.sample.domain.dto.system.UploadFile;

import lombok.Getter;
import lombok.Setter;

@Table(name = "users")
@Entity
@Getter
@Setter
public class User extends DomaDtoImpl {

    private static final long serialVersionUID = 4512633005852272922L;

    @OriginalStates // 差分UPDATEのために定義する
    @JsonIgnore // APIのレスポンスに含めない
    User originalStates;

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    // ハッシュ化されたパスワード
    @JsonIgnore
    String password;

    // 名前
    String firstName;

    // 苗字
    String lastName;

    // メールアドレス
    @Email
    String email;

    // 電話番号
    @Digits(fraction = 0, integer = 10)
    String tel;

    // 郵便番号
    @NotEmpty
    String zip;

    // 住所
    @NotEmpty
    String address;

    // 添付ファイルID
    @JsonIgnore
    Long uploadFileId;

    // 添付ファイル
    @Transient // Domaで永続化しない
    @JsonIgnore
    UploadFile uploadFile;
}
