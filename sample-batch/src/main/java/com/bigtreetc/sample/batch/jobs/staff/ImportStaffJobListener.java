package com.bigtreetc.sample.batch.jobs.staff;

import com.bigtreetc.sample.batch.context.BatchContext;
import com.bigtreetc.sample.batch.listener.BaseJobExecutionListener;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import lombok.val;
import org.springframework.batch.core.JobExecution;

public class ImportStaffJobListener extends BaseJobExecutionListener {

  @Override
  protected String getBatchId() {
    return "BATCH_001";
  }

  @Override
  protected String getBatchName() {
    return "担当者情報取込";
  }

  @Override
  protected void before(JobExecution jobExecution, BatchContext context) {
    // 前日を対象とする
    val yesterday = LocalDateTime.now().minus(1, ChronoUnit.DAYS);
    context.getAttributes().putIfAbsent("targetDate", yesterday);
  }

  @Override
  protected void after(JobExecution jobExecution, BatchContext context) {
    // 終了する直前に呼び出される
  }
}
