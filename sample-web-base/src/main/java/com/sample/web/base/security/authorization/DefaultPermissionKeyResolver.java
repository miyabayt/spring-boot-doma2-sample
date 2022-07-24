package com.sample.web.base.security.authorization;

import lombok.val;
import org.springframework.web.method.HandlerMethod;

/** コントローラーのメソッド名から権限キーを解決する */
public class DefaultPermissionKeyResolver implements PermissionKeyResolver {

  private static final String TYPE_NAME_REPLACEMENT = "HtmlController";

  @Override
  public String resolve(Object handler) {
    String permissionKey = null;

    if (handler instanceof HandlerMethod) {
      val handlerMethod = (HandlerMethod) handler;
      val bean = handlerMethod.getBean();

      if (bean instanceof Authorizable) {
        val typeName = handlerMethod.getBeanType().getSimpleName();
        val typeNamePrefix = typeName.replace(TYPE_NAME_REPLACEMENT, "");
        val methodName = handlerMethod.getMethod().getName();

        if (((Authorizable) bean).authorityRequired()) {
          permissionKey = String.format("%s.%s", typeNamePrefix, methodName);
        }
      }
    }

    return permissionKey;
  }
}
