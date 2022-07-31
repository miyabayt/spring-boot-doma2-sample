package com.sample.domain.dto.user;

import com.sample.domain.dto.common.DomaDtoImpl;
import lombok.Getter;
import lombok.Setter;
import org.seasar.doma.*;

@Table(name = "user_roles")
@Entity
@Getter
@Setter
public class UserRole extends DomaDtoImpl {

  private static final long serialVersionUID = -6750983302974218054L;

  // 担当者ロールID
  @Id
  @Column(name = "user_role_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  // ユーザーID
  Long userId;

  // ロールコード
  String roleCode;

  // ロール名
  String roleName;

  // 権限コード
  String permissionCode;

  // 権限名
  String permissionName;
}
