package com.bigtreetc.sample.batch.jobs;

import com.bigtreetc.sample.batch.context.BatchContext;
import com.bigtreetc.sample.batch.context.BatchContextHolder;
import java.util.List;
import lombok.val;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.lang.NonNull;

public abstract class BaseItemWriter<T> implements ItemWriter<T> {

  @SuppressWarnings("unchecked")
  @Override
  public void write(@NonNull Chunk<? extends T> chunk) throws Exception {
    // コンテキストを取り出す
    val context = BatchContextHolder.getContext();

    // 書き込む
    doWrite(context, (List<T>) chunk.getItems());
  }

  /**
   * 引数に渡されたアイテムを書き込みます。
   *
   * @param context
   * @param items
   */
  protected abstract void doWrite(BatchContext context, List<T> items);
}
