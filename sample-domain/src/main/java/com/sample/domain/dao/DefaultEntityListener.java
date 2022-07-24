package com.sample.domain.dao;

import static java.util.stream.Collectors.toList;

import com.sample.common.util.ReflectionUtils;
import com.sample.domain.dto.common.DomaDto;
import com.sample.domain.dto.common.Dto;
import com.sample.domain.exception.DoubleSubmitErrorException;
import java.util.List;
import java.util.Objects;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.seasar.doma.Id;
import org.seasar.doma.jdbc.entity.EntityListener;
import org.seasar.doma.jdbc.entity.PreDeleteContext;
import org.seasar.doma.jdbc.entity.PreInsertContext;
import org.seasar.doma.jdbc.entity.PreUpdateContext;

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

    if (entity instanceof DomaDto) {
      val domaDto = (DomaDto) entity;
      val createdAt = AuditInfoHolder.getAuditDateTime();
      val createdBy = AuditInfoHolder.getAuditUser();

      domaDto.setCreatedAt(createdAt); // 作成日
      domaDto.setCreatedBy(createdBy); // 作成者
    }
  }

  @Override
  public void preUpdate(ENTITY entity, PreUpdateContext<ENTITY> context) {

    if (entity instanceof DomaDto) {
      val domaDto = (DomaDto) entity;
      val updatedAt = AuditInfoHolder.getAuditDateTime();
      val updatedBy = AuditInfoHolder.getAuditUser();

      val methodName = context.getMethod().getName();
      if (methodName.startsWith("delete")) {
        domaDto.setDeletedAt(updatedAt); // 削除日
        domaDto.setDeletedBy(updatedBy); // 削除者
      } else {
        domaDto.setUpdatedAt(updatedAt); // 更新日
        domaDto.setUpdatedBy(updatedBy); // 更新者
      }
    }
  }

  @Override
  public void preDelete(ENTITY entity, PreDeleteContext<ENTITY> context) {

    if (entity instanceof DomaDto) {
      val domaDto = (DomaDto) entity;
      val deletedAt = AuditInfoHolder.getAuditDateTime();
      val deletedBy = AuditInfoHolder.getAuditUser();
      val name = domaDto.getClass().getName();
      val ids = getIds(domaDto);

      // 物理削除した場合はログ出力する
      log.info(
          "データを物理削除しました。entity={}, id={}, deletedBy={}, deletedAt={}",
          name,
          ids,
          deletedBy,
          deletedAt);
    }
  }

  /**
   * Idアノテーションが付与されたフィールドの値のリストを返します。
   *
   * @param dto
   * @return
   */
  protected List<Object> getIds(Dto dto) {
    return ReflectionUtils.findWithAnnotation(dto.getClass(), Id.class)
        .map(f -> ReflectionUtils.getFieldValue(f, dto))
        .collect(toList());
  }
}
