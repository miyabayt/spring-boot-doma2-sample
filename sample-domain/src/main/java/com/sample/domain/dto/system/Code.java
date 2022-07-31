package com.sample.domain.dto.system;

import com.sample.domain.dto.common.DomaDtoImpl;
import lombok.Getter;
import lombok.Setter;
import org.seasar.doma.*;

@Table(name = "codes")
@Entity
@Getter
@Setter
public class Code extends DomaDtoImpl {

  private static final long serialVersionUID = 8207242972390517957L;

  // コードID
  @Id
  @Column(name = "code_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  // 分類コード
  String categoryCode;

  // コード分類名
  String categoryName;

  // コード値
  String codeValue;

  // コード名
  String codeName;

  // エイリアス
  String codeAlias;

  // 表示順
  Integer displayOrder;
}
