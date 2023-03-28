package com.bigtreetc.sample.domain.entity;

import com.bigtreetc.sample.domain.entity.common.DomaEntityImpl;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.seasar.doma.*;

@Table(name = "users")
@Entity
@Getter
@Setter
public class User extends DomaEntityImpl {

  private static final long serialVersionUID = 4512633005852272922L;

  @OriginalStates // 差分UPDATEのために定義する
  @JsonIgnore // APIのレスポンスに含めない
  User originalStates;

  @Id
  @Column(name = "user_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  // ハッシュ化されたパスワード
  @JsonIgnore String password;

  // 名
  String firstName;

  // 姓
  String lastName;

  // メールアドレス
  @Email String email;

  // 電話番号
  @Digits(fraction = 0, integer = 10)
  String tel;

  // 郵便番号
  @NotEmpty String zip;

  // 住所
  @NotEmpty String address;

  // 画像ファイルID
  @JsonIgnore Long uploadFileId;

  // 画像ファイル
  @Transient // Domaで永続化しない
  @JsonIgnore
  UploadFile uploadFile;
}
