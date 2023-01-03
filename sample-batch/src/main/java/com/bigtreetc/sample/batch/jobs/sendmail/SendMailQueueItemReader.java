package com.bigtreetc.sample.batch.jobs.sendmail;

import static java.util.stream.Collectors.toList;

import com.bigtreetc.sample.batch.jobs.BasePageableItemReader;
import com.bigtreetc.sample.domain.dao.SendMailQueueDao;
import com.bigtreetc.sample.domain.entity.SendMailQueue;
import com.bigtreetc.sample.domain.entity.SendMailQueueCriteria;
import java.util.List;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;

/** メール送信対象を検索する */
public class SendMailQueueItemReader extends BasePageableItemReader<SendMailQueue> {

  @Autowired SendMailQueueDao sendMailQueueDao;

  @Override
  protected List<SendMailQueue> getList() {
    val criteria = new SendMailQueueCriteria();
    val options = getSelectOptions();
    return sendMailQueueDao.selectAll(criteria, options, toList());
  }
}
