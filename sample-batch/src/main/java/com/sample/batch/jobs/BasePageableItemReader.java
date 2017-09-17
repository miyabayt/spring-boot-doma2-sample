package com.sample.batch.jobs;

import static com.sample.batch.BatchConst.MDC_BATCH_ID;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.seasar.doma.jdbc.SelectOptions;
import org.springframework.batch.item.database.AbstractPagingItemReader;

import com.sample.batch.context.BatchContextHolder;
import com.sample.common.util.MDCUtils;
import com.sample.domain.dao.AuditInfoHolder;

import lombok.val;

/**
 * ページングに対応したItemReaderの基底クラス
 * 
 * @param <T>
 */
public abstract class BasePageableItemReader<T> extends AbstractPagingItemReader<T> {

    @Override
    protected T doRead() throws Exception {
        val context = BatchContextHolder.getContext();
        val batchId = context.getBatchId();

        // ThreadPoolを使用している場合は再設定する
        MDCUtils.putIfAbsent(MDC_BATCH_ID, batchId);

        val startDateTime = context.getStartDateTime();
        AuditInfoHolder.set(batchId, startDateTime);

        return super.doRead();
    }

    @Override
    protected void doReadPage() {
        if (results == null) {
            results = new CopyOnWriteArrayList<>();
        } else {
            results.clear();
        }

        results.addAll(getList());
    }

    @Override
    protected void doJumpToPage(int itemIndex) {
    }

    /**
     * 検索オプションを返します。
     * 
     * @return
     */
    protected SelectOptions getSelectOptions() {
        val page = getPage(); // 1ページは0になる
        val perpage = getPageSize();
        val offset = page * perpage;
        return SelectOptions.get().offset(offset).limit(perpage);
    }

    /**
     * ページング処理したリストを返します。
     * 
     * @return
     */
    protected abstract List<T> getList();
}
