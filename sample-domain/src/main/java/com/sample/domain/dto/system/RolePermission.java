package com.sample.domain.dto.system;

import org.seasar.doma.*;

import com.sample.domain.dto.common.DomaDtoImpl;
import com.sample.domain.dto.common.ID;

import lombok.Getter;
import lombok.Setter;

@Table(name = "role_permissions")
@Entity
@Getter
@Setter
public class RolePermission extends DomaDtoImpl {

    private static final long serialVersionUID = 4915898548766398327L;

    @OriginalStates // 差分UPDATEのために定義する
    RolePermission originalStates;

    @Id
    @Column(name = "role_permission_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    ID<RolePermission> id;

    // 役割キー
    String roleKey;

    // 権限ID
    Integer permissionId;
}
