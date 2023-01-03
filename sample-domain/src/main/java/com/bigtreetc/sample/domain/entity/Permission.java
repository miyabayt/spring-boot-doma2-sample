package com.bigtreetc.sample.domain.entity;

import com.bigtreetc.sample.domain.entity.common.DomaEntityImpl;
import lombok.Getter;
import lombok.Setter;
import org.seasar.doma.*;

@Table(name = "permissions")
@Entity
@Getter
@Setter
public class Permission extends DomaEntityImpl {

  private static final long serialVersionUID = -258501373358638948L;

  // 権限ID
  @Id
  @Column(name = "permission_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  // 権限コード
  String permissionCode;

  // 権限名
  String permissionName;
}
