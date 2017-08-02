package com.sample.batch.jobs.staff;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.springframework.batch.core.JobExecution;

import com.sample.batch.context.BatchContext;
import com.sample.batch.listener.BaseJobExecutionListener;

import lombok.val;

public class ImportStaffJobListener extends BaseJobExecutionListener {

    @Override
    protected String getBatchId() {
        return "BATCH_001";
    }

    @Override
    protected String getBatchName() {
        return "担当者情報取り込み";
    }

    @Override
    protected void before(JobExecution jobExecution, BatchContext context) {
        // 前日を対象とする
        val yesterday = LocalDateTime.now().minus(1, ChronoUnit.DAYS);
        context.getAdditionalInfo().putIfAbsent("targetDate", yesterday);
    }

    @Override
    protected void after(JobExecution jobExecution, BatchContext context) {
        // 終了する直前に呼び出される
    }
}
