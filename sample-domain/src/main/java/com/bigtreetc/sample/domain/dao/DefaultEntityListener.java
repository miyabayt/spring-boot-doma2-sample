package com.bigtreetc.sample.domain.dao;

import static java.util.stream.Collectors.toList;

import com.bigtreetc.sample.common.util.ReflectionUtils;
import com.bigtreetc.sample.domain.entity.common.DomaEntity;
import com.bigtreetc.sample.domain.entity.common.IEntity;
import com.bigtreetc.sample.domain.exception.DoubleSubmitErrorException;
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

    if (entity instanceof DomaEntity domaEntity) {
      val createdAt = AuditInfoHolder.getAuditDateTime();
      val createdBy = AuditInfoHolder.getAuditUser();

      domaEntity.setCreatedAt(createdAt); // 作成日
      domaEntity.setCreatedBy(createdBy); // 作成者
    }
  }

  @Override
  public void preUpdate(ENTITY entity, PreUpdateContext<ENTITY> context) {

    if (entity instanceof DomaEntity domaEntity) {
      val updatedAt = AuditInfoHolder.getAuditDateTime();
      val updatedBy = AuditInfoHolder.getAuditUser();

      val methodName = context.getMethod().getName();
      if (methodName.startsWith("delete")) {
        domaEntity.setDeletedAt(updatedAt); // 削除日
        domaEntity.setDeletedBy(updatedBy); // 削除者
      } else {
        domaEntity.setUpdatedAt(updatedAt); // 更新日
        domaEntity.setUpdatedBy(updatedBy); // 更新者
      }
    }
  }

  @Override
  public void preDelete(ENTITY entity, PreDeleteContext<ENTITY> context) {

    if (entity instanceof DomaEntity domaEntity) {
      val deletedAt = AuditInfoHolder.getAuditDateTime();
      val deletedBy = AuditInfoHolder.getAuditUser();
      val name = domaEntity.getClass().getName();
      val ids = getIds(domaEntity);

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
   * @param entity
   * @return
   */
  protected List<Object> getIds(IEntity entity) {
    return ReflectionUtils.findWithAnnotation(entity.getClass(), Id.class)
        .map(f -> ReflectionUtils.getFieldValue(f, entity))
        .collect(toList());
  }
}
