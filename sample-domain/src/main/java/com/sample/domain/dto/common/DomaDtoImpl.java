package com.sample.domain.dto.common;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.Transient;
import org.seasar.doma.Version;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sample.domain.dao.DefaultEntityListener;

import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("serial")
@Entity(listener = DefaultEntityListener.class) // 自動的にシステム制御項目を更新するためにリスナーを指定する
@Setter
@Getter
public abstract class DomaDtoImpl implements DomaDto, Serializable {

    // 作成者
    @JsonIgnore
    String createdBy;

    // 作成日時
    @JsonIgnore
    LocalDateTime createdAt;

    // 更新者
    @JsonIgnore
    String updatedBy;

    // 更新日時
    @JsonIgnore
    LocalDateTime updatedAt;

    // 削除者
    @JsonIgnore
    String deletedBy;

    // 削除日時
    @JsonIgnore
    LocalDateTime deletedAt;

    // 楽観的排他制御で使用する改定番号
    @Version
    @Column(name = "version")
    @JsonIgnore
    Integer version;

    // 作成・更新者に使用する値
    @Transient
    @JsonIgnore
    String auditUser;

    // 作成・更新日に使用する値
    @Transient
    @JsonIgnore
    LocalDateTime auditDateTime;

    // 更新されたプロパティ名
    @Transient
    @JsonIgnore
    List<String> changedProperties;

    // 更新項目名
    @Transient
    @JsonIgnore
    String changedPropertyNames;

    // 更新者ID
    @Transient
    @JsonIgnore
    Integer changedBy;

    // 更新日時
    @Transient
    @JsonIgnore
    LocalDateTime changedAt;

    /**
     * 既存レコードがないデータであるか
     * 
     * @return
     */
    @JsonIgnore
    public boolean isNew() {
        return getId().equals(ID.notAssigned());
    }

    /**
     * IdカラムのGetter
     * 
     * @return
     */
    @JsonIgnore
    public abstract ID<? extends DomaDto> getId();
}
