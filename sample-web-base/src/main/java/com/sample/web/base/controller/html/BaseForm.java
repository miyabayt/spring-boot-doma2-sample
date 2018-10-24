package com.sample.web.base.controller.html;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class BaseForm implements Serializable {

    private static final long serialVersionUID = 893506941860422885L;

    // 作成・更新者に使用する値
    String auditUser;

    // 作成・更新日に使用する値
    LocalDateTime auditDateTime;

    // 改定番号
    Integer version;

    /**
     * 既存レコードがないデータであるか
     *
     * @return
     */
    public boolean isNew() {
        return getId() == null;
    }

    /**
     * IdカラムのGetter
     *
     * @return
     */
    public abstract Long getId();
}
