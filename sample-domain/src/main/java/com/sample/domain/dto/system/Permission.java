package com.sample.domain.dto.system;

import com.sample.domain.dto.common.DomaDtoImpl;
import lombok.Getter;
import lombok.Setter;
import org.seasar.doma.*;

@Table(name = "permissions")
@Entity
@Getter
@Setter
public class Permission extends DomaDtoImpl {

  private static final long serialVersionUID = -258501373358638948L;

  // 権限ID
  @Id
  @Column(name = "permission_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  // 権限カテゴリキー
  String categoryKey;

  // 権限キー
  String permissionKey;

  // 権限名
  String permissionName;
}
