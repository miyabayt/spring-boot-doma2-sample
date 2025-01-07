package com.bigtreetc.sample.batch.listener;

import static com.bigtreetc.sample.batch.BatchConst.MDC_BATCH_ID;
import static com.bigtreetc.sample.domain.Const.YYYY_MM_DD_HHmmss;

import com.bigtreetc.sample.batch.context.BatchContext;
import com.bigtreetc.sample.batch.context.BatchContextHolder;
import com.bigtreetc.sample.common.util.DateUtils;
import com.bigtreetc.sample.common.util.MDCUtils;
import com.bigtreetc.sample.domain.dao.AuditInfoHolder;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

@Slf4j
public abstract class BaseJobExecutionListener implements JobExecutionListener {

  @Override
  public void beforeJob(JobExecution jobExecution) {
    val batchId = getBatchId();
    val batchName = getBatchName();
    val startTime = jobExecution.getStartTime();

    MDCUtils.putIfAbsent(MDC_BATCH_ID, batchId);

    log.info("*********************************************");
    log.info("* バッチID : {}", batchId);
    log.info("* バッチ名 : {}", batchName);
    log.info("* 開始時刻 : {}", DateUtils.format(startTime, YYYY_MM_DD_HHmmss));
    log.info("*********************************************");

    // 監査情報を設定する
    AuditInfoHolder.set(batchId, startTime);

    // コンテキストを設定する
    val context = BatchContextHolder.getContext();
    context.set(batchId, batchName, startTime);

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
      val batchId = context.getBatchId();
      val batchName = context.getBatchName();
      val jobStatus = jobExecution.getStatus();
      val startTime = jobExecution.getStartTime();
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
            took(jobExecution.getStartTime(), jobExecution.getEndTime()));

        jobExecution
            .getStepExecutions()
            .forEach(
                s ->
                    log.debug(
                        "step executed. [step={}] in {}ms",
                        s.getStepName(),
                        took(s.getStartTime(), s.getEndTime())));
      }

      if (!jobStatus.isRunning()) {
        log.info("*********************************************");
        log.info("* バッチID\t\t: {}", batchId);
        log.info("* バッチ名\t\t: {}", batchName);
        log.info("* ステータス\t\t: {}", jobStatus);
        log.info("* 対象件数\t\t: {}", context.getTotalCount());
        log.info("* 処理件数\t\t: {}", context.getProcessCount());
        log.info("* エラー件数\t\t: {}", context.getErrorCount());
        log.info("* 開始時刻\t\t: {}", DateUtils.format(startTime, YYYY_MM_DD_HHmmss));
        log.info("* 終了時刻\t\t: {}", DateUtils.format(endTime, YYYY_MM_DD_HHmmss));
        log.info("*********************************************");
      }
    }
  }

  protected long took(LocalDateTime startTime, LocalDateTime endTime) {
    if (startTime == null || endTime == null) return -1;
    return TimeUnit.NANOSECONDS.toMillis(startTime.getNano() - endTime.getNano());
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
