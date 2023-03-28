package com.bigtreetc.sample.batch.listener;

import static com.bigtreetc.sample.batch.BatchConst.MDC_BATCH_ID;

import com.bigtreetc.sample.batch.context.BatchContext;
import com.bigtreetc.sample.batch.context.BatchContextHolder;
import com.bigtreetc.sample.common.util.MDCUtils;
import com.bigtreetc.sample.domain.dao.AuditInfoHolder;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

@Slf4j
public abstract class BaseStepExecutionListener implements StepExecutionListener {

  @Override
  public void beforeStep(StepExecution stepExecution) {
    val context = BatchContextHolder.getContext();

    // MDCを設定する
    setMDCIfEmpty(context, stepExecution);

    // 監査情報を設定する
    setAuditInfoIfEmpty(context);

    // 機能別の初期化処理を呼び出す
    before(context, stepExecution);

    // ログ出力
    logBeforeStep(context, stepExecution);
  }

  @Override
  public ExitStatus afterStep(StepExecution stepExecution) {
    val context = BatchContextHolder.getContext();

    // 機能別の終了処理を呼び出す
    try {
      after(context, stepExecution);
    } catch (Exception e) {
      log.error("exception occurred. ", e);
      throw new IllegalStateException(e);
    }

    // ログ出力
    logAfterStep(context, stepExecution);
    ExitStatus exitStatus = stepExecution.getExitStatus();
    return exitStatus;
  }

  /**
   * MDCのThreadLocal変数が取り出せないときは必要に応じて設定しなおす。
   *
   * @param context
   * @param stepExecution
   */
  protected void setMDCIfEmpty(BatchContext context, StepExecution stepExecution) {
    val batchId = context.getBatchId();
    MDCUtils.putIfAbsent(MDC_BATCH_ID, batchId);
  }

  /**
   * スレッドプールを使用しているとThreadLocalから値が取り出せないことがあるため設定しなおす。
   *
   * @param context
   */
  protected void setAuditInfoIfEmpty(BatchContext context) {
    val batchId = context.getBatchId();
    val startDateTime = context.getStartDateTime();

    AuditInfoHolder.set(batchId, startDateTime);
  }

  /**
   * ステップ開始時にログを出力します。
   *
   * @param context
   * @param stepExecution
   */
  protected void logBeforeStep(BatchContext context, StepExecution stepExecution) {
    String stepName = stepExecution.getStepName();
    log.info("Step:{} ---- START ----", stepName);
  }

  /**
   * ステップ終了時にログを出力します。
   *
   * @param context
   * @param stepExecution
   */
  protected void logAfterStep(BatchContext context, StepExecution stepExecution) {
    String stepName = stepExecution.getStepName();
    log.info("Step:{} ---- END ----", stepName);
  }

  /**
   * 機能別の初期化処理を呼び出します。
   *
   * @param context
   * @param stepExecution
   */
  protected void before(BatchContext context, StepExecution stepExecution) {}

  /**
   * 機能別の終了処理を呼び出します。
   *
   * @param context
   * @param stepExecution
   */
  protected void after(BatchContext context, StepExecution stepExecution) {}
}
