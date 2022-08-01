package com.sample.domain.dto.system;

import com.sample.domain.dto.common.DomaDtoImpl;
import lombok.Getter;
import lombok.Setter;
import org.seasar.doma.*;

@Table(name = "code_categories")
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

  // 分類コード
  String categoryCode;

  // 分類名
  String categoryName;
}
