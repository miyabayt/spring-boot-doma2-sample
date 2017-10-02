package com.sample.domain.service.system;

import static java.util.stream.Collectors.toList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.sample.domain.dao.system.MailTemplateDao;
import com.sample.domain.dto.common.ID;
import com.sample.domain.dto.common.Page;
import com.sample.domain.dto.common.PageFactory;
import com.sample.domain.dto.common.Pageable;
import com.sample.domain.dto.system.MailTemplate;
import com.sample.domain.exception.NoDataFoundException;
import com.sample.domain.service.BaseTransactionalService;

import lombok.val;

/**
 * メールテンプレートサービス
 */
@Service
public class MailTemplateService extends BaseTransactionalService {

    @Autowired
    MailTemplateDao mailTemplateDao;

    /**
     * メールテンプレートを一括取得します。
     *
     * @return
     */
    @Transactional(readOnly = true) // 読み取りのみの場合は指定する
    public Page<MailTemplate> findAll(MailTemplate where, Pageable pageable) {
        Assert.notNull(where, "where must not be null");

        // ページングを指定する
        val options = createSearchOptions(pageable).count();
        val mailTemplates = mailTemplateDao.selectAll(where, options, toList());

        return PageFactory.create(mailTemplates, pageable, options.getCount());
    }

    /**
     * メールテンプレートを取得します。
     *
     * @return
     */
    @Transactional(readOnly = true)
    public MailTemplate findById(final ID<MailTemplate> id) {
        // 1件取得
        val mailTemplate = mailTemplateDao.selectById(id)
                .orElseThrow(() -> new NoDataFoundException("mailTemplate_id=" + id + " のデータが見つかりません。"));
        return mailTemplate;
    }

    /**
     * メールテンプレートを追加します。
     *
     * @param inputMailTemplate
     * @return
     */
    public MailTemplate create(final MailTemplate inputMailTemplate) {
        Assert.notNull(inputMailTemplate, "inputMailTemplate must not be null");

        // 1件登録
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
        Assert.notNull(inputMailTemplate, "inputMailTemplate must not be null");

        // 1件更新
        int updated = mailTemplateDao.update(inputMailTemplate);

        if (updated < 1) {
            throw new NoDataFoundException("mailTemplate_id=" + inputMailTemplate.getId() + " のデータが見つかりません。");
        }

        return inputMailTemplate;
    }

    /**
     * メールテンプレートを論理削除します。
     *
     * @return
     */
    public MailTemplate delete(final ID<MailTemplate> id) {
        val mailTemplate = mailTemplateDao.selectById(id)
                .orElseThrow(() -> new NoDataFoundException("mailTemplate_id=" + id + " のデータが見つかりません。"));

        int updated = mailTemplateDao.delete(mailTemplate);

        if (updated < 1) {
            throw new NoDataFoundException("mailTemplate_id=" + id + " は更新できませんでした。");
        }

        return mailTemplate;
    }
}
