package com.bigtreetc.sample.domain.repository;

import static com.bigtreetc.sample.domain.util.DomaUtils.createSelectOptions;
import static java.util.stream.Collectors.toList;

import com.bigtreetc.sample.domain.dao.MailTemplateDao;
import com.bigtreetc.sample.domain.entity.MailTemplate;
import com.bigtreetc.sample.domain.entity.MailTemplateCriteria;
import com.bigtreetc.sample.domain.exception.NoDataFoundException;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

/** メールテンプレートリポジトリ */
@RequiredArgsConstructor
@Repository
public class MailTemplateRepository {

  @NonNull final MailTemplateDao mailTemplateDao;

  /**
   * メールテンプレートを検索します。
   *
   * @param criteria
   * @param pageable
   * @return
   */
  public Page<MailTemplate> findAll(MailTemplateCriteria criteria, Pageable pageable) {
    val options = createSelectOptions(pageable).count();
    val data = mailTemplateDao.selectAll(criteria, options, toList());
    return new PageImpl<>(data, pageable, options.getCount());
  }

  /**
   * メールテンプレートを検索します。
   *
   * @return
   */
  public Stream<MailTemplate> findAll(MailTemplateCriteria criteria) {
    return mailTemplateDao.selectAll(criteria);
  }

  /**
   * メールテンプレートを1件取得します。
   *
   * @param criteria
   * @return
   */
  public Optional<MailTemplate> findOne(MailTemplateCriteria criteria) {
    return mailTemplateDao.select(criteria);
  }

  /**
   * メールテンプレートを1件取得します。
   *
   * @return
   */
  public MailTemplate findById(final Long id) {
    return mailTemplateDao
        .selectById(id)
        .orElseThrow(() -> new NoDataFoundException("mailTemplate_id=" + id + " のデータが見つかりません。"));
  }

  /**
   * メールテンプレートを登録します。
   *
   * @param inputMailTemplate
   * @return
   */
  public MailTemplate create(final MailTemplate inputMailTemplate) {
    mailTemplateDao.insert(inputMailTemplate);
    return inputMailTemplate;
  }

  /**
   * メールテンプレートを更新します。
   *
   * @param inputMailTemplate
   * @return
   */
  public MailTemplate update(final MailTemplate inputMailTemplate) {
    int updated = mailTemplateDao.update(inputMailTemplate);

    if (updated < 1) {
      throw new NoDataFoundException(
          "mailTemplate_id=" + inputMailTemplate.getId() + " のデータが見つかりません。");
    }

    return inputMailTemplate;
  }

  /**
   * メールテンプレートを論理削除します。
   *
   * @return
   */
  public MailTemplate delete(final Long id) {
    val mailTemplate =
        mailTemplateDao
            .selectById(id)
            .orElseThrow(
                () -> new NoDataFoundException("mailTemplate_id=" + id + " のデータが見つかりません。"));

    int updated = mailTemplateDao.delete(mailTemplate);

    if (updated < 1) {
      throw new NoDataFoundException("mailTemplate_id=" + id + " は更新できませんでした。");
    }

    return mailTemplate;
  }
}
