package com.sample.domain.dto.system;

import static com.sample.common.util.ValidateUtils.isEquals;

import com.sample.domain.dto.common.DomaDtoImpl;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.seasar.doma.*;

@Table(name = "roles")
@Entity
@Getter
@Setter
public class Role extends DomaDtoImpl {

  private static final long serialVersionUID = 4825745231712286767L;

  @OriginalStates // 差分UPDATEのために定義する
  Role originalStates;

  @Id
  @Column(name = "role_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  // ロールコード
  String roleCode;

  // ロール名
  String roleName;

  final @Transient List<RolePermission> rolePermissions = new ArrayList<>();

  final @Transient List<Permission> permissions = new ArrayList<>();

  public boolean hasPermission(String permissionCode) {
    return rolePermissions.stream()
        .anyMatch(rp -> isEquals(rp.getPermissionCode(), permissionCode) && rp.getIsEnabled());
  }

  public void setPermission(String permissionCode, boolean isEnabled) {
    rolePermissions.stream()
        .filter(rp -> isEquals(rp.getPermissionCode(), permissionCode))
        .findFirst()
        .ifPresent(rp -> rp.setIsEnabled(isEnabled));
  }
}
