package com.bigtreetc.sample.batch.jobs.sendmail;

import com.bigtreetc.sample.batch.context.BatchContext;
import com.bigtreetc.sample.batch.listener.BaseJobExecutionListener;
import org.springframework.batch.core.JobExecution;

public class SendMailJobListener extends BaseJobExecutionListener {

  @Override
  protected String getBatchId() {
    return "BATCH_004";
  }

  @Override
  protected String getBatchName() {
    return "メール送信";
  }

  @Override
  protected void before(JobExecution jobExecution, BatchContext context) {}

  @Override
  protected void after(JobExecution jobExecution, BatchContext context) {
    // 終了する直前に呼び出される
  }
}
