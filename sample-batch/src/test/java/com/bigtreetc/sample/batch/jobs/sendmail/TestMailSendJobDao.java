package com.bigtreetc.sample.batch.jobs.sendmail;

import com.bigtreetc.sample.domain.entity.SendMailQueue;
import java.util.stream.Collector;
import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.SelectType;
import org.seasar.doma.Sql;
import org.seasar.doma.boot.ConfigAutowireable;

@ConfigAutowireable
@Dao
public interface TestMailSendJobDao {

  @Sql(
      """
      SELECT
        *
      FROM
        send_mail_queue
      WHERE
        to_address = /* toAddress */'test@example.com'
      """)
  @Select(strategy = SelectType.COLLECT)
  <R> R selectAll(String toAddress, Collector<SendMailQueue, ?, R> collector);

  @Sql(
      """
      SELECT
        count(*)
      FROM
        send_mail_queue
      WHERE
        sent_at IS NULL
        AND from_address = /* fromAddress */'from@example.com'
      """)
  @Select
  int countByFromAddressAndSentAtIsNull(String fromAddress);

  @Sql(
      """
      SELECT
        count(*)
      FROM
        send_mail_queue
      WHERE
        sent_at IS NOT NULL
        AND from_address = /* fromAddress */'from@example.com'
      """)
  @Select
  int countByFromAddressAndSentAtIsNotNull(String fromAddress);
}
