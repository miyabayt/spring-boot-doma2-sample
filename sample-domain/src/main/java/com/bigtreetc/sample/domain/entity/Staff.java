package com.bigtreetc.sample.domain.entity;

import com.bigtreetc.sample.domain.entity.common.DomaEntityImpl;
import java.time.LocalDateTime;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;
import org.seasar.doma.*;

@Table(name = "staffs")
@Entity
@Getter
@Setter
public class Staff extends DomaEntityImpl {

  private static final long serialVersionUID = -3762941082070995608L;

  @OriginalStates // 差分UPDATEのために定義する
  Staff originalStates;

  @Id
  @Column(name = "staff_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  String password;

  // 名
  String firstName;

  // 姓
  String lastName;

  // メールアドレス
  @Email String email;

  // 電話番号
  @Digits(fraction = 0, integer = 10)
  String tel;

  // パスワードリセットトークン
  String passwordResetToken;

  // トークン失効日
  LocalDateTime tokenExpiresAt;
}
