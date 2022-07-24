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

  // 担当者役割ID
  @Id
  @Column(name = "staff_role_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  // 担当者ID
  Long staffId;

  // 役割キー
  String roleKey;

  // 役割名
  String roleName;

  // 権限キー
  String permissionKey;

  // 権限名
  String permissionName;
}
