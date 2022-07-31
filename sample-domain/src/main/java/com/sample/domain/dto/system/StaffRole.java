package com.sample.domain.dto.system;

import com.sample.domain.dto.common.DomaDtoImpl;
import lombok.Getter;
import lombok.Setter;
import org.seasar.doma.*;

@Table(name = "staff_roles")
@Entity
@Getter
@Setter
public class StaffRole extends DomaDtoImpl {

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
