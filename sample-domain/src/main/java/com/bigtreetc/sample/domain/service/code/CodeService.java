package com.bigtreetc.sample.domain.service.code;

import com.bigtreetc.sample.domain.entity.Code;
import com.bigtreetc.sample.domain.entity.CodeCriteria;
import com.bigtreetc.sample.domain.exception.NoDataFoundException;
import com.bigtreetc.sample.domain.repository.CodeRepository;
import com.bigtreetc.sample.domain.service.BaseTransactionalService;
import com.bigtreetc.sample.domain.util.CsvUtils;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Optional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/** コードマスタサービス */
@RequiredArgsConstructor
@Service
public class CodeService extends BaseTransactionalService {

  @NonNull final CodeRepository codeRepository;

  /**
   * コードマスタを検索します。
   *
   * @param criteria
   * @param pageable
   * @return
   */
  @Transactional(readOnly = true) // 読み取りのみの場合は指定する
  public Page<Code> findAll(CodeCriteria criteria, Pageable pageable) {
    Assert.notNull(criteria, "criteria must not be null");
    return codeRepository.findAll(criteria, pageable);
  }

  /**
   * コードマスタを1件取得します。
   *
   * @return
   */
  @Transactional(readOnly = true)
  public Optional<Code> findOne(CodeCriteria criteria) {
    Assert.notNull(criteria, "criteria must not be null");
    return codeRepository.findOne(criteria);
  }

  /**
   * コードマスタを1件取得します。
   *
   * @return
   */
  @Transactional(readOnly = true)
  public Code findById(final Long id) {
    Assert.notNull(id, "id must not be null");
    return codeRepository.findById(id);
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
    val criteria = new CodeCriteria();
    criteria.setCategoryCode(categoryCode);
    return codeRepository
        .findOne(criteria)
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
   * コードマスタを更新します。
   *
   * @param inputCode
   * @return
   */
  public Code update(final Code inputCode) {
    Assert.notNull(inputCode, "inputCode must not be null");
    return codeRepository.update(inputCode);
  }

  /**
   * コードマスタを論理削除します。
   *
   * @return
   */
  public Code delete(final Long id) {
    Assert.notNull(id, "id must not be null");
    return codeRepository.delete(id);
  }

  /**
   * コードマスタを書き出します。
   *
   * @param outputStream
   * @param
   * @return
   */
  @Transactional(readOnly = true) // 読み取りのみの場合は指定する
  public void writeToOutputStream(OutputStream outputStream, CodeCriteria criteria, Class<?> clazz)
      throws IOException {
    Assert.notNull(criteria, "criteria must not be null");
    try (val data = codeRepository.findAll(criteria)) {
      CsvUtils.writeCsv(outputStream, clazz, data, code -> modelMapper.map(code, clazz));
    }
  }
}
