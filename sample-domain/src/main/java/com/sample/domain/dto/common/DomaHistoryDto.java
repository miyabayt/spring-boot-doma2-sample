package com.sample.domain.dto.common;

import java.time.LocalDateTime;

public interface DomaHistoryDto {

    Integer getChangedId();

    void setChangedId(Integer changedId);

    String getChangedPropertyNames();

    void setChangedPropertyNames(String changedPropertyNames);

    Integer getChangedBy();

    void setChangedBy(Integer changedBy);

    LocalDateTime getChangedAt();

    void setChangedAt(LocalDateTime changedAt);
}
