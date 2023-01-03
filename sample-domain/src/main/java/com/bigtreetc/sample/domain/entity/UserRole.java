package com.bigtreetc.sample.domain.entity;

import com.bigtreetc.sample.domain.entity.common.DomaEntityImpl;
import lombok.Getter;
import lombok.Setter;
import org.seasar.doma.*;

@Table(name = "user_roles")
@Entity
@Getter
@Setter
public class UserRole extends DomaEntityImpl {

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
