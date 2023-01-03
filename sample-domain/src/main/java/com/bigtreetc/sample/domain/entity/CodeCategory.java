package com.bigtreetc.sample.domain.entity;

import com.bigtreetc.sample.domain.entity.common.DomaEntityImpl;
import lombok.Getter;
import lombok.Setter;
import org.seasar.doma.*;

@Table(name = "code_categories")
@Entity
@Getter
@Setter
public class CodeCategory extends DomaEntityImpl {

  private static final long serialVersionUID = 2229749282619203935L;

  // コード分類ID
  @Id
  @Column(name = "code_category_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  // 分類コード
  String categoryCode;

  // 分類名
  String categoryName;
}
