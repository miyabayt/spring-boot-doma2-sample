package com.sample.batch.listener;

import static com.sample.batch.BatchConst.*;
import static com.sample.domain.Const.YYYY_MM_DD_HHmmss;

import org.slf4j.MDC;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;

import com.sample.common.util.LocalDateTimeUtils;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultJobExecutionListener extends JobExecutionListenerSupport {

    @Override
    public void beforeJob(JobExecution jobExecution) {
        val jobInstance = jobExecution.getJobInstance();
        val startTime = jobExecution.getStartTime();

        log.info("*********************************************");
        log.info("* 処理名　　　: {}", jobInstance.getId());
        log.info("* バッチID　　: {}", jobInstance.getJobName());
        log.info("* 開始日時　　: {}", LocalDateTimeUtils.format(startTime, YYYY_MM_DD_HHmmss));
        log.info("*********************************************");

        MDC.put(MDC_BATCH_ID, String.valueOf(jobExecution.getId()));
        MDC.put(MDC_BATCH_JOB_ID, String.valueOf(jobExecution.getJobId()));
        MDC.put(MDC_BATCH_JOB_NAME, jobInstance.getJobName());
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        try {
            val jobInstance = jobExecution.getJobInstance();
            val jobStatus = jobExecution.getStatus();
            val endTime = jobExecution.getEndTime();

            if (log.isDebugEnabled()) {
                log.debug("job executed. [job={}(JobInstanceId:{} status:{})] in {}ms", jobInstance.getJobName(),
                        jobInstance.getId(), jobStatus, took(jobExecution));
                jobExecution.getStepExecutions()
                        .forEach(s -> log.debug("step executed. [step={}] in {}ms", s.getStepName(), took(s)));
            }

            if (jobStatus == BatchStatus.COMPLETED) {
                log.info("*********************************************");
                log.info("* 処理名　　　: {}", jobInstance.getId());
                log.info("* バッチID　　: {}", jobInstance.getJobName());
                log.info("* 終了日時　　: {}", LocalDateTimeUtils.format(endTime, YYYY_MM_DD_HHmmss));
                log.info("*********************************************");
            }

        } finally {
            MDC.remove(MDC_BATCH_ID);
            MDC.remove(MDC_BATCH_JOB_ID);
            MDC.remove(MDC_BATCH_JOB_NAME);
        }
    }

    protected long took(JobExecution jobExecution) {
        return jobExecution.getEndTime().getTime() - jobExecution.getStartTime().getTime();
    }

    protected long took(StepExecution stepExecution) {
        return stepExecution.getEndTime().getTime() - stepExecution.getStartTime().getTime();
    }
}
