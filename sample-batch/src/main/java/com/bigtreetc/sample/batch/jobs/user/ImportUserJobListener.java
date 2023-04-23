package com.bigtreetc.sample.batch.jobs.user;

import com.bigtreetc.sample.batch.listener.BaseJobExecutionListener;

public class ImportUserJobListener extends BaseJobExecutionListener {

  @Override
  protected String getBatchId() {
    return "BATCH_002";
  }

  @Override
  protected String getBatchName() {
    return "顧客マスタ取込";
  }
}
