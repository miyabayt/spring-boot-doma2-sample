package com.bigtreetc.sample.batch.context;

public class BatchContextHolder {

  private static final BatchContext CONTEXT = new BatchContext();

  /**
   * コンテキストを返します。
   *
   * @return
   */
  public static BatchContext getContext() {
    return CONTEXT;
  }
}
