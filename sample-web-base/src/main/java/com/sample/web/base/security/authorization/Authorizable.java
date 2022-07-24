package com.sample.web.base.security.authorization;

public interface Authorizable {

  /**
   * 認可を必要とする機能の場合はtrueを返します。
   *
   * @return
   */
  boolean authorityRequired();
}
