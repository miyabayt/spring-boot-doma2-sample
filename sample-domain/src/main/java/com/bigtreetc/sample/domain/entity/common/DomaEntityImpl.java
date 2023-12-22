package com.bigtreetc.sample.domain.entity.common;

import com.bigtreetc.sample.domain.dao.DefaultEntityListener;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.Transient;
import org.seasar.doma.Version;

@SuppressWarnings("serial")
@Entity(listener = DefaultEntityListener.class) // 自動的にシステム制御項目を更新するためにリスナーを指定する
@Setter
@Getter
public abstract class DomaEntityImpl implements DomaEntity, Serializable {

  // 作成者
  @Column(updatable = false)
  @JsonIgnore
  String createdBy;

  // 作成日時
  @Column(updatable = false)
  LocalDateTime createdAt;

  // 更新者
  @JsonIgnore String updatedBy;

  // 更新日時
  LocalDateTime updatedAt;

  // 削除者
  @JsonIgnore String deletedBy;

  // 削除日時
  LocalDateTime deletedAt;

  // 楽観的排他制御で使用する改定番号
  @Version
  @Column(name = "version")
  Integer version;

  // 作成・更新者に使用する値
  @Transient @JsonIgnore String auditUser;

  // 作成・更新日に使用する値
  @Transient @JsonIgnore LocalDateTime auditDateTime;
}
