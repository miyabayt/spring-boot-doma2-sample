package com.bigtreetc.sample.web.api.base.resource;

/** リソースファクトリのデフォルト実装 */
public class DefaultResourceFactoryImpl implements ResourceFactory {

  /**
   * インスタンスを作成します。
   *
   * @return
   */
  @Override
  public Resource create() {
    return new ResourceImpl();
  }
}
