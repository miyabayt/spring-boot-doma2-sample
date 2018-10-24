package com.sample.domain.dao.system;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;

import org.seasar.doma.*;
import org.seasar.doma.boot.ConfigAutowireable;
import org.seasar.doma.jdbc.SelectOptions;

import com.sample.domain.dto.system.MailTemplate;
import com.sample.domain.dto.system.MailTemplateCriteria;

@ConfigAutowireable
@Dao
public interface MailTemplateDao {

    /**
     * メールテンプレートを取得します。
     *
     * @param criteria
     * @param options
     * @return
     */
    @Select(strategy = SelectType.COLLECT)
    <R> R selectAll(final MailTemplateCriteria criteria, final SelectOptions options,
            final Collector<MailTemplate, ?, R> collector);

    /**
     * メールテンプレートを1件取得します。
     *
     * @param id
     * @return
     */
    @Select
    Optional<MailTemplate> selectById(Long id);

    /**
     * メールテンプレートを1件取得します。
     *
     * @param criteria
     * @return
     */
    @Select
    Optional<MailTemplate> select(MailTemplateCriteria criteria);

    /**
     * メールテンプレートを登録します。
     *
     * @param mailtemplate
     * @return
     */
    @Insert
    int insert(MailTemplate mailtemplate);

    /**
     * メールテンプレートを更新します。
     *
     * @param mailTemplate
     * @return
     */
    @Update
    int update(MailTemplate mailTemplate);

    /**
     * メールテンプレートを論理削除します。
     *
     * @param mailTemplate
     * @return
     */
    @Update(excludeNull = true)
    // NULLの項目は更新対象にしない
    int delete(MailTemplate mailTemplate);

    /**
     * メールテンプレートを一括登録します。
     *
     * @param mailTemplates
     * @return
     */
    @BatchInsert
    int[] insert(List<MailTemplate> mailTemplates);

    /**
     * メールテンプレートを一括更新します。
     *
     * @param mailtemplates
     * @return
     */
    @BatchUpdate
    int[] update(List<MailTemplate> mailtemplates);
}
