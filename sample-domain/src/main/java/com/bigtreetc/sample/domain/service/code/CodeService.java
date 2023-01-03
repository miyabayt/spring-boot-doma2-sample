package com.bigtreetc.sample.domain.service.code;

import com.bigtreetc.sample.common.util.ValidateUtils;
import com.bigtreetc.sample.domain.entity.Code;
import com.bigtreetc.sample.domain.entity.CodeCriteria;
import com.bigtreetc.sample.domain.exception.NoDataFoundException;
import com.bigtreetc.sample.domain.repository.CodeRepository;
import com.bigtreetc.sample.domain.service.BaseTransactionalService;
import java.util.Optional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/** コードサービス */
@RequiredArgsConstructor
@Service
public class CodeService extends BaseTransactionalService {

  @NonNull final CodeRepository codeRepository;

  /**
   * コードを複数取得します。
   *
   * @return
   */
  @Transactional(readOnly = true) // 読み取りのみの場合は指定する
  public Page<Code> findAll(CodeCriteria criteria, Pageable pageable) {
    Assert.notNull(criteria, "criteria must not be null");
    return codeRepository.findAll(criteria, pageable);
  }

  /**
   * コードを取得します。
   *
   * @return
   */
  @Transactional(readOnly = true)
  public Optional<Code> findOne(CodeCriteria criteria) {
    Assert.notNull(criteria, "criteria must not be null");
    return codeRepository.findOne(criteria);
  }

  /**
   * コードを取得します。
   *
   * @return
   */
  @Transactional(readOnly = true)
  public Code findById(final Long id) {
    Assert.notNull(id, "id must not be null");
    return codeRepository.fetchAll().stream()
        .filter(c -> c.getId() == id.longValue())
        .findFirst()
        .orElseThrow(() -> new NoDataFoundException("id=" + id + " のデータが見つかりません。"));
  }

  /**
   * コードを取得します。
   *
   * @param categoryCode
   * @return
   */
  @Transactional(readOnly = true)
  public Code findByCategoryCode(final String categoryCode) {
    Assert.notNull(categoryCode, "categoryCode must not be null");
    return codeRepository.fetchAll().stream()
        .filter(c -> ValidateUtils.isEquals(categoryCode, c.getCategoryCode()))
        .findFirst()
        .orElseThrow(
            () -> new NoDataFoundException("category_code=" + categoryCode + " のデータが見つかりません。"));
  }

  /**
   * コードを追加します。
   *
   * @param inputCode
   * @return
   */
  public Code create(final Code inputCode) {
    Assert.notNull(inputCode, "inputCode must not be null");
    return codeRepository.create(inputCode);
  }

  /**
   * コードを更新します。
   *
   * @param inputCode
   * @return
   */
  public Code update(final Code inputCode) {
    Assert.notNull(inputCode, "inputCode must not be null");
    return codeRepository.update(inputCode);
  }

  /**
   * コードを論理削除します。
   *
   * @return
   */
  public Code delete(final Long id) {
    Assert.notNull(id, "id must not be null");
    return codeRepository.delete(id);
  }
}
