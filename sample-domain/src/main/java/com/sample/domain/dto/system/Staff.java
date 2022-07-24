package com.sample.domain.dto.system;

import com.sample.domain.dto.common.DomaDtoImpl;
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
public class Staff extends DomaDtoImpl {

  private static final long serialVersionUID = -3762941082070995608L;

  @OriginalStates // 差分UPDATEのために定義する
  Staff originalStates;

  @Id
  @Column(name = "staff_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  String password;

  // 名前
  String firstName;

  // 苗字
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
