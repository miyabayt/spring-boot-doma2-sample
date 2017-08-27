package com.sample.domain.dto;

import org.seasar.doma.*;

import com.sample.domain.dto.common.DomaDtoImpl;
import com.sample.domain.dto.common.ID;

import lombok.Getter;
import lombok.Setter;

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
    ID<Role> id;

    String roleKey;

    String roleName;

    ID<Permission> permissionId;
}
