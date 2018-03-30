package com.sample.domain.dao.system;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;

import org.seasar.doma.*;
import org.seasar.doma.boot.ConfigAutowireable;
import org.seasar.doma.jdbc.SelectOptions;

import com.sample.domain.dto.system.MailTemplate;

@ConfigAutowireable
@Dao
public interface MailTemplateDao {

    /**
     * メールテンプレートを取得します。
     *
     * @param mailTemplate
     * @param options
     * @return
     */
    @Select(strategy = SelectType.COLLECT)
    <R> R selectAll(final MailTemplate mailTemplate, final SelectOptions options,
            final Collector<MailTemplate, ?, R> collector);

    /**
     * メールテンプレートを1件取得します。
     *
     * @param id
     * @return
     */
    @Select
    Optional<MailTemplate> selectById(Integer id);

    /**
     * メールテンプレートを1件取得します。
     *
     * @param mailTemplate
     * @return
     */
    @Select
    Optional<MailTemplate> select(MailTemplate mailTemplate);

    /**
     * メールテンプレートを登録します。
     *
     * @param MailTemplate
     * @return
     */
    @Insert
    int insert(MailTemplate MailTemplate);

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
}
