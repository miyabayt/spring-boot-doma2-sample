package com.sample.batch.jobs;

import java.util.List;

import org.springframework.batch.item.ItemWriter;

import com.sample.batch.context.BatchContext;
import com.sample.batch.context.BatchContextHolder;

import lombok.val;

public abstract class BaseItemWriter<T> implements ItemWriter<T> {

    @SuppressWarnings("unchecked")
    @Override
    public void write(List<? extends T> items) throws Exception {
        // コンテキストを取り出す
        val context = BatchContextHolder.getContext();

        // 書き込む
        doWrite(context, (List<T>) items);
    }

    /**
     * 引数に渡されたアイテムを書き込みます。
     *
     * @param context
     * @param items
     */
    protected abstract void doWrite(BatchContext context, List<T> items);
}
