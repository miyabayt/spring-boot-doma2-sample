package com.bigtreetc.sample.domain.entity;

import com.bigtreetc.sample.domain.entity.common.DomaEntityImpl;
import lombok.Getter;
import lombok.Setter;
import org.seasar.doma.*;

@Table(name = "codes")
@Entity
@Getter
@Setter
public class Code extends DomaEntityImpl {

  private static final long serialVersionUID = 8207242972390517957L;

  // コードID
  @Id
  @Column(name = "code_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  // 分類コード
  String categoryCode;

  // コード分類名
  @Column(insertable = false, updatable = false)
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
