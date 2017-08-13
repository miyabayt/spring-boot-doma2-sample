package com.sample.domain.dao.listener;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.seasar.doma.jdbc.entity.EntityListener;
import org.seasar.doma.jdbc.entity.PreDeleteContext;
import org.seasar.doma.jdbc.entity.PreInsertContext;
import org.seasar.doma.jdbc.entity.PreUpdateContext;

import com.sample.domain.dto.common.DomaDto;
import com.sample.domain.exception.DoubleSubmitErrorException;

import lombok.NoArgsConstructor;
import lombok.val;
import lombok.extern.slf4j.Slf4j;

@NoArgsConstructor // コンストラクタが必須のため
@Slf4j
public class DefaultEntityListener<ENTITY> implements EntityListener<ENTITY> {

    @Override
    public void preInsert(ENTITY entity, PreInsertContext<ENTITY> context) {
        // 二重送信防止チェック
        val expected = DoubleSubmitCheckTokenHolder.getExpectedToken();
        val actual = DoubleSubmitCheckTokenHolder.getActualToken();

        if (expected != null && actual != null && !Objects.equals(expected, actual)) {
            throw new DoubleSubmitErrorException();
        }

        val domaDto = getDomaDto(entity);
        val createdAt = AuditInfoHolder.getAuditDateTime();
        val createdBy = AuditInfoHolder.getAuditUser();

        domaDto.setCreatedAt(createdAt); // 作成日
        domaDto.setCreatedBy(createdBy); // 作成者
    }

    @Override
    public void preUpdate(ENTITY entity, PreUpdateContext<ENTITY> context) {
        val domaDto = getDomaDto(entity);
        val updatedAt = AuditInfoHolder.getAuditDateTime();
        val updatedBy = AuditInfoHolder.getAuditUser();

        val methodName = context.getMethod().getName();
        if (StringUtils.startsWith("delete", methodName)) {
            domaDto.setDeletedAt(updatedAt); // 削除日
            domaDto.setDeletedBy(updatedBy); // 削除者
        } else {
            domaDto.setUpdatedAt(updatedAt); // 更新日
            domaDto.setUpdatedBy(updatedBy); // 更新者
        }
    }

    @Override
    public void preDelete(ENTITY entity, PreDeleteContext<ENTITY> context) {
        val domaDto = getDomaDto(entity);
        val deletedAt = AuditInfoHolder.getAuditDateTime();
        val deletedBy = AuditInfoHolder.getAuditUser();
        val canonicalName = domaDto.getClass().getCanonicalName();

        // 物理削除した場合はログ出力する
        log.info("{} id={}, deletedBy={}, deletedAt={}", canonicalName, domaDto.getId(), deletedBy,
                deletedAt.toString());
    }

    private DomaDto getDomaDto(ENTITY entity) {
        return (DomaDto) entity;
    }
}
