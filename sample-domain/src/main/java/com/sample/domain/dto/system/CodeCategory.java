package com.sample.domain.dto.system;

import com.sample.domain.dto.common.DomaDtoImpl;
import lombok.Getter;
import lombok.Setter;
import org.seasar.doma.*;

@Table(name = "code_category")
@Entity
@Getter
@Setter
public class CodeCategory extends DomaDtoImpl {

  private static final long serialVersionUID = 2229749282619203935L;

  // コード分類ID
  @Id
  @Column(name = "code_category_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

  // カテゴリキー
  String categoryKey;

  // カテゴリ名
  String categoryName;
}
