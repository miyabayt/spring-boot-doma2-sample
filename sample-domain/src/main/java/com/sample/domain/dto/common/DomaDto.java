package com.sample.domain.dto.common;

import java.time.LocalDateTime;
import java.util.List;

// TODO: コメントを書く
public interface DomaDto extends Dto {

    String getCreatedBy();

    void setCreatedBy(String createdBy);

    LocalDateTime getCreatedAt();

    void setCreatedAt(LocalDateTime createdAt);

    String getUpdatedBy();

    void setUpdatedBy(String updatedBy);

    LocalDateTime getUpdatedAt();

    void setUpdatedAt(LocalDateTime updatedAt);

    String getDeletedBy();

    void setDeletedBy(String deletedBy);

    LocalDateTime getDeletedAt();

    void setDeletedAt(LocalDateTime deletedAt);

    Integer getVersion();

    void setVersion(Integer version);

    List<String> getChangedProperties();

    void setChangedProperties(List<String> changedProperties);

    Integer getChangedBy();

    void setChangedBy(Integer changedBy);

    LocalDateTime getChangedAt();

    void setChangedAt(LocalDateTime changedAt);

    boolean isNew();

    ID<? extends DomaDto> getId();
}
