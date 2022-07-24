package com.sample.batch.jobs;

import com.sample.batch.context.BatchContext;
import com.sample.batch.context.BatchContextHolder;
import com.sample.batch.item.ItemPosition;
import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.modelmapper.ModelMapper;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.annotation.OnProcessError;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.validator.SpringValidator;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Validator;

/** 基底タスクレット */
@Slf4j
public abstract class BaseTasklet<I extends ItemPosition> implements Tasklet {

  @Autowired protected ModelMapper modelMapper;

  @Autowired
  @Qualifier("beanValidator")
  protected Validator beanValidator;

  /**
   * メインメソッド
   *
   * @param contribution
   * @param chunkContext
   * @return
   * @throws Exception
   */
  public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext)
      throws IOException {
    val itemValidator = getValidator();
    val context = BatchContextHolder.getContext();

    val springValidator = new SpringValidator<I>();
    springValidator.setValidator(itemValidator);

    // 処理対象を読み込む
    val streams = doRead(context);

    for (int i = 0; i < streams.size(); i++) {
      try (val stream = streams.get(i)) {
        int[] idx = {1};

        stream.forEach(
            item -> {
              // 最初・最後のフラグを立てる
              setItemPosition(item, idx[0]);

              // 対象件数を加算する
              increaseTotalCount(context, item);

              val binder = new DataBinder(item);
              binder.addValidators(beanValidator, itemValidator);
              binder.validate();

              val br = binder.getBindingResult();
              if (br.hasErrors()) {
                // エラー件数をカウントする
                increaseErrorCount(context, br, item);

                // バリデーションエラーがある場合
                onValidationError(context, br, item);
              }

              if (!br.hasErrors()) {
                // 実処理
                doProcess(context, item);

                // 処理件数をカウントする
                increaseProcessCount(context, item);
              }

              idx[0]++;
            });
      }
    }

    return RepeatStatus.FINISHED;
  }

  /**
   * 最初・最後のフラグを設定します。
   *
   * @param itemPosition
   * @param i
   */
  protected void setItemPosition(ItemPosition itemPosition, int i) {
    // 行数を設定する
    itemPosition.setPosition(i);
  }

  /**
   * エラー件数を加算します。
   *
   * @param context
   * @param br
   * @param item
   */
  protected void increaseErrorCount(BatchContext context, BindingResult br, I item) {
    context.increaseErrorCount();
  }

  /**
   * 処理件数を加算します。
   *
   * @param context
   * @param item
   */
  protected void increaseProcessCount(BatchContext context, I item) {
    context.increaseProcessCount();
  }

  /**
   * 対象件数を加算します。
   *
   * @param context
   * @param item
   */
  protected void increaseTotalCount(BatchContext context, I item) {
    context.increaseTotalCount();
  }

  /**
   * バリデーションエラーが発生した場合に処理します。
   *
   * @param context
   * @param br
   * @param item
   */
  protected abstract void onValidationError(BatchContext context, BindingResult br, I item);

  /**
   * 実処理を実施します。
   *
   * @param context
   * @return
   */
  protected abstract List<Stream<I>> doRead(BatchContext context) throws IOException;

  /**
   * 実処理を実施します。
   *
   * @param context
   * @param item
   * @return
   */
  protected abstract void doProcess(BatchContext context, I item);

  /**
   * バリデーターを取得します。
   *
   * @return
   */
  protected abstract Validator getValidator();

  /**
   * 例外発生時のデフォルト実装
   *
   * @param item
   * @param e
   */
  @OnProcessError
  protected void onProcessError(I item, Exception e) {
    log.error("failed to process item.", e);
    throw new IllegalStateException(e);
  }
}
