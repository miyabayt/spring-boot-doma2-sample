package com.sample.web.base.security.authorization;

public interface PermissionKeyResolver {

  /**
   * 権限キーを解決します。
   *
   * @param handler
   * @return
   */
  String resolve(Object handler);
}
