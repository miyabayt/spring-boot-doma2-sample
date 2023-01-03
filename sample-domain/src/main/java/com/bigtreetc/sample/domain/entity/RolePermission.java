package com.bigtreetc.sample.domain.entity;

import com.bigtreetc.sample.domain.entity.common.DomaEntityImpl;
import lombok.Getter;
import lombok.Setter;
import org.seasar.doma.*;

@Table(name = "role_permissions")
@Entity
@Getter
@Setter
public class RolePermission extends DomaEntityImpl {

  private static final long serialVersionUID = 4915898548766398327L;

  @OriginalStates // 差分UPDATEのために定義する
  RolePermission originalStates;

  @Id
  @Column(name = "role_permission_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  // ロールコード
  String roleCode;

  // 権限コード
  String permissionCode;

  // 有効
  Boolean isEnabled;
}
