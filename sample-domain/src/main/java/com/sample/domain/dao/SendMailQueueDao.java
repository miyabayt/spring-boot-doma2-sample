package com.sample.domain.dao;

import java.util.List;
import java.util.stream.Collector;

import org.seasar.doma.*;
import org.seasar.doma.boot.ConfigAutowireable;
import org.seasar.doma.jdbc.SelectOptions;

import com.sample.domain.dto.SendMailQueue;

@ConfigAutowireable
@Dao
public interface SendMailQueueDao {

    /**
     * メール送信キューを取得します。
     *
     * @param options
     * @return
     */
    @Select(strategy = SelectType.COLLECT)
    <R> R selectAll(final SelectOptions options, final Collector<SendMailQueue, ?, R> collector);

    /**
     * メール送信キューを一括登録します。
     *
     * @param sendMailQueues
     * @return
     */
    @BatchInsert
    int[] insert(List<SendMailQueue> sendMailQueues);

    /**
     * メール送信キューを一括更新します。
     *
     * @param sendMailQueues
     * @return
     */
    @BatchUpdate
    int[] update(List<SendMailQueue> sendMailQueues);
}
