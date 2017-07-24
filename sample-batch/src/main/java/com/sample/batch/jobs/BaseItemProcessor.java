package com.sample.batch.jobs;

import org.springframework.batch.core.annotation.OnProcessError;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Validator;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

/**
 * 基底プロセッサー
 * 
 * @param <I>
 * @param <O>
 */
@Slf4j
public abstract class BaseItemProcessor<I, O> implements ItemProcessor<I, O> {

    @Override
    public O process(I item) {
        val validator = getValidator();

        if (validator != null) {
            val binder = new DataBinder(item);
            binder.setValidator(validator);
            binder.validate();

            val result = binder.getBindingResult();
            if (result.hasErrors()) {
                // バリデーションエラーがある場合
                onValidationError(result, item);
                return null; // ItemProcessorでnullを返すと後続のWriterで処理されない
            }
        }

        // 実処理
        O output = doProcess(item);

        return output;
    }

    /**
     * バリデーションエラーが発生した場合に処理します。
     * 
     * @param result
     * @param item
     */
    protected abstract void onValidationError(BindingResult result, I item);

    /**
     * 実処理を実施します。
     *
     * @param item
     * @return
     */
    protected abstract O doProcess(I item);

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
