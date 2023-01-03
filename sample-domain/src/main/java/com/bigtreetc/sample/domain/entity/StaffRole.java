package com.bigtreetc.sample.domain.entity;

import com.bigtreetc.sample.domain.entity.common.DomaEntityImpl;
import lombok.Getter;
import lombok.Setter;
import org.seasar.doma.*;

@Table(name = "staff_roles")
@Entity
@Getter
@Setter
public class StaffRole extends DomaEntityImpl {

  private static final long serialVersionUID = 1780669742437422350L;

  // 担当者ロールID
  @Id
  @Column(name = "staff_role_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  // 担当者ID
  Long staffId;

  // ロールコード
  String roleCode;

  // ロール名
  String roleName;

  // 権限コード
  String permissionCode;

  // 権限名
  String permissionName;
}
