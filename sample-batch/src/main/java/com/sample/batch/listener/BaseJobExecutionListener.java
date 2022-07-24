package com.sample.batch.listener;

import static com.sample.batch.BatchConst.MDC_BATCH_ID;
import static com.sample.domain.Const.YYYY_MM_DD_HHmmss;

import com.sample.batch.context.BatchContext;
import com.sample.batch.context.BatchContextHolder;
import com.sample.common.util.DateUtils;
import com.sample.common.util.MDCUtils;
import com.sample.domain.dao.AuditInfoHolder;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.slf4j.MDC;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;

@Slf4j
public abstract class BaseJobExecutionListener extends JobExecutionListenerSupport {

  @Override
  public void beforeJob(JobExecution jobExecution) {
    val batchId = getBatchId();
    val batchName = getBatchName();
    val startTime = jobExecution.getStartTime();
    val startDateTime = DateUtils.toLocalDateTime(startTime);

    MDCUtils.putIfAbsent(MDC_BATCH_ID, batchId);

    log.info("*********************************************");
    log.info("* バッチID : {}", batchId);
    log.info("* バッチ名 : {}", batchName);
    log.info("* 開始時刻 : {}", DateUtils.format(startTime, YYYY_MM_DD_HHmmss));
    log.info("*********************************************");

    // 監査情報を設定する
    AuditInfoHolder.set(batchId, startDateTime);

    // コンテキストを設定する
    val context = BatchContextHolder.getContext();
    context.set(batchId, batchName, startDateTime);

    // 機能別の初期化処理を呼び出す
    before(jobExecution, context);
  }

  @Override
  public void afterJob(JobExecution jobExecution) {
    // コンテキストを取り出す
    val context = BatchContextHolder.getContext();

    // 機能別の終了処理を呼び出す
    try {
      after(jobExecution, context);
    } catch (Throwable t) {
      log.error("exception occurred. ", t);
      throw new IllegalStateException(t);
    } finally {
      // 共通の終了処理
      try {
        val batchId = context.getBatchId();
        val batchName = context.getBatchName();
        val jobStatus = jobExecution.getStatus();
        val endTime = jobExecution.getEndTime();

        if (log.isDebugEnabled()) {
          val jobId = jobExecution.getJobId();
          val jobInstance = jobExecution.getJobInstance();
          val jobInstanceId = jobInstance.getInstanceId();
          log.debug(
              "job executed. [job={}(JobInstanceId:{} status:{})] in {}ms",
              jobId,
              jobInstanceId,
              jobStatus,
              took(jobExecution));
          jobExecution
              .getStepExecutions()
              .forEach(
                  s -> log.debug("step executed. [step={}] in {}ms", s.getStepName(), took(s)));
        }

        if (!jobStatus.isRunning()) {
          log.info("*********************************************");
          log.info("* バッチID   : {}", batchId);
          log.info("* バッチ名   : {}", batchName);
          log.info("* ステータス : {}", jobStatus.getBatchStatus().toString());
          log.info("* 対象件数   : {}", context.getTotalCount());
          log.info("* 処理件数   : {}", context.getProcessCount());
          log.info("* エラー件数 : {}", context.getErrorCount());
          log.info("* 終了時刻   : {}", DateUtils.format(endTime, YYYY_MM_DD_HHmmss));
          log.info("*********************************************");
        }
      } finally {
        MDC.remove(MDC_BATCH_ID);

        // 監査情報をクリアする
        AuditInfoHolder.clear();

        // ジョブコンテキストをクリアする
        context.clear();
      }
    }
  }

  protected long took(JobExecution jobExecution) {
    return jobExecution.getEndTime().getTime() - jobExecution.getStartTime().getTime();
  }

  protected long took(StepExecution stepExecution) {
    return stepExecution.getEndTime().getTime() - stepExecution.getStartTime().getTime();
  }

  /**
   * バッチIDを返します。
   *
   * @return
   */
  protected abstract String getBatchId();

  /**
   * バッチ名を返します。
   *
   * @return
   */
  protected abstract String getBatchName();

  /**
   * 機能別の初期化処理を呼び出す
   *
   * @param jobExecution
   * @param context
   */
  protected void before(JobExecution jobExecution, BatchContext context) {}

  /**
   * 機能別の終了処理を呼び出す
   *
   * @param jobExecution
   * @param context
   */
  protected void after(JobExecution jobExecution, BatchContext context) {}
}
