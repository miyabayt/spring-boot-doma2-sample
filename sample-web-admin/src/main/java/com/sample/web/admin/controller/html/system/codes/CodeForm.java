package com.sample.web.admin.controller.html.system.codes;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.sample.web.base.controller.html.BaseForm;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CodeForm extends BaseForm {

    private static final long serialVersionUID = 7555305356779221873L;

    Integer id;

    // コードキー
    @NotEmpty
    String codeKey;

    // コード名
    @NotEmpty
    String codeValue;

    // コードエイリアス
    @NotEmpty
    String codeAlias;

    // 表示順
    @NotNull
    Integer displayOrder;

    // 無効フラグ
    Boolean isInvalid;
}
