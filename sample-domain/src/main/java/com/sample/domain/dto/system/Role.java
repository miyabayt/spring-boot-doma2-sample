package com.sample.domain.dto.system;

import com.sample.domain.dto.common.DomaDtoImpl;
import java.util.Map;
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

  // 役割キー
  String roleKey;

  // 役割名
  String roleName;

  // 権限
  @Transient Map<Integer, Boolean> permissions;
}
