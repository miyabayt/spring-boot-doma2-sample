package com.bigtreetc.sample.domain.service.mailtemplate;

import com.bigtreetc.sample.domain.entity.MailTemplate;
import com.bigtreetc.sample.domain.entity.MailTemplateCriteria;
import com.bigtreetc.sample.domain.repository.MailTemplateRepository;
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

/** メールテンプレートサービス */
@RequiredArgsConstructor
@Service
public class MailTemplateService extends BaseTransactionalService {

  @NonNull final MailTemplateRepository mailTemplateRepository;

  /**
   * メールテンプレートを検索します。
   *
   * @param criteria
   * @param pageable
   * @return
   */
  @Transactional(readOnly = true) // 読み取りのみの場合は指定する
  public Page<MailTemplate> findAll(MailTemplateCriteria criteria, Pageable pageable) {
    Assert.notNull(criteria, "criteria must not be null");
    return mailTemplateRepository.findAll(criteria, pageable);
  }

  /**
   * メールテンプレートを1件取得します。
   *
   * @return
   */
  @Transactional(readOnly = true)
  public Optional<MailTemplate> findOne(MailTemplateCriteria criteria) {
    Assert.notNull(criteria, "criteria must not be null");
    return mailTemplateRepository.findOne(criteria);
  }

  /**
   * メールテンプレートを1件取得します。
   *
   * @return
   */
  @Transactional(readOnly = true)
  public MailTemplate findById(final Long id) {
    Assert.notNull(id, "id must not be null");
    return mailTemplateRepository.findById(id);
  }

  /**
   * メールテンプレートを追加します。
   *
   * @param inputMailTemplate
   * @return
   */
  public MailTemplate create(final MailTemplate inputMailTemplate) {
    Assert.notNull(inputMailTemplate, "inputMailTemplate must not be null");
    return mailTemplateRepository.create(inputMailTemplate);
  }

  /**
   * メールテンプレートを更新します。
   *
   * @param inputMailTemplate
   * @return
   */
  public MailTemplate update(final MailTemplate inputMailTemplate) {
    Assert.notNull(inputMailTemplate, "inputMailTemplate must not be null");
    return mailTemplateRepository.update(inputMailTemplate);
  }

  /**
   * メールテンプレートを論理削除します。
   *
   * @return
   */
  public MailTemplate delete(final Long id) {
    Assert.notNull(id, "id must not be null");
    return mailTemplateRepository.delete(id);
  }

  /**
   * メールテンプレートを書き出します。
   *
   * @param outputStream
   * @param
   * @return
   */
  @Transactional(readOnly = true) // 読み取りのみの場合は指定する
  public void writeToOutputStream(
      OutputStream outputStream, MailTemplateCriteria criteria, Class<?> clazz) throws IOException {
    Assert.notNull(criteria, "criteria must not be null");
    try (val data = mailTemplateRepository.findAll(criteria)) {
      CsvUtils.writeCsv(
          outputStream, clazz, data, mailTemplate -> modelMapper.map(mailTemplate, clazz));
    }
  }
}
