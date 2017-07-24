package com.sample.domain.dao.listener;

import org.apache.commons.lang3.StringUtils;
import org.seasar.doma.jdbc.entity.EntityListener;
import org.seasar.doma.jdbc.entity.PreDeleteContext;
import org.seasar.doma.jdbc.entity.PreInsertContext;
import org.seasar.doma.jdbc.entity.PreUpdateContext;

import com.sample.domain.dto.common.DomaDto;

import lombok.NoArgsConstructor;
import lombok.val;
import lombok.extern.slf4j.Slf4j;

@NoArgsConstructor // コンストラクタが必須のため
@Slf4j
public class DefaultEntityListener<ENTITY> implements EntityListener<ENTITY> {

    @Override
    public void preInsert(ENTITY entity, PreInsertContext<ENTITY> context) {
        val domaDto = getDomaDto(entity);

        // 作成日
        domaDto.setCreatedAt(AuditInfoHolder.getAuditDateTime());
        // 作成者
        domaDto.setCreatedBy(AuditInfoHolder.getAuditUser());
    }

    @Override
    public void preUpdate(ENTITY entity, PreUpdateContext<ENTITY> context) {
        val domaDto = getDomaDto(entity);

        val methodName = context.getMethod().getName();

        if (StringUtils.startsWith("delete", methodName)) {
            // 削除日
            domaDto.setDeletedAt(AuditInfoHolder.getAuditDateTime());
            // 削除者
            domaDto.setDeletedBy(AuditInfoHolder.getAuditUser());
        } else {
            // 更新日
            domaDto.setUpdatedAt(AuditInfoHolder.getAuditDateTime());
            // 更新者
            domaDto.setUpdatedBy(AuditInfoHolder.getAuditUser());
        }
    }

    @Override
    public void preDelete(ENTITY entity, PreDeleteContext<ENTITY> context) {
        val domaDto = getDomaDto(entity);
        val canonicalName = domaDto.getClass().getCanonicalName();

        // 物理削除した場合はログ出力する
        log.info("{} id={}, deletedBy={}, deletedAt={}", canonicalName, domaDto.getId(), AuditInfoHolder.getAuditUser(),
                AuditInfoHolder.getAuditDateTime().toString());
    }

    private DomaDto getDomaDto(ENTITY entity) {
        return (DomaDto) entity;
    }
}
