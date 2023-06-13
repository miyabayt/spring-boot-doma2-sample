package com.bigtreetc.sample.web.api.base.resource;

/** リソースファクトリ */
public interface ResourceFactory {

  /**
   * インスタンスを作成します。
   *
   * @return
   */
  Resource create();
}
