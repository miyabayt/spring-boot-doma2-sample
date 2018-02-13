package com.sample.domain.dto.common;

import java.time.LocalDateTime;

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
}
