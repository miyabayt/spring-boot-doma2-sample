package com.sample.domain.dto.system;

import org.seasar.doma.*;
import org.springframework.cache.annotation.Cacheable;

import com.sample.domain.dto.common.DomaDtoImpl;
import com.sample.domain.dto.common.ID;

import lombok.Getter;
import lombok.Setter;

@Table(name = "code_category")
@Entity
@Getter
@Setter
@Cacheable
public class CodeCategory extends DomaDtoImpl {

    private static final long serialVersionUID = 2229749282619203935L;

    // コード分類ID
    @Id
    @Column(name = "code_category_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    ID<CodeCategory> id;

    // カテゴリキー
    String categoryKey;

    // カテゴリ名
    String categoryName;
}
