package com.bigtreetc.sample.web.base.aop;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.val;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/** 基底インターセプター */
public abstract class BaseHandlerInterceptor implements HandlerInterceptor {

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    // コントローラーの動作前
    return true;
  }

  @Override
  public void postHandle(
      HttpServletRequest request,
      HttpServletResponse response,
      Object handler,
      ModelAndView modelAndView)
      throws Exception {
    // コントローラーの動作後

  }

  @Override
  public void afterCompletion(
      HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
      throws Exception {
    // 処理完了後
  }

  /**
   * 引数のオブジェクトが指定のクラスであるかどうかを示すを返します。
   *
   * @param obj
   * @param clazz
   * @return
   */
  protected boolean isInstanceOf(Object obj, Class<?> clazz) {

    if (clazz.isAssignableFrom(obj.getClass())) {
      return true;
    }

    return false;
  }

  /**
   * HandlerのBeanを返します。
   *
   * @param handler
   * @return
   */
  @SuppressWarnings("unchecked")
  protected <T> T getBean(Object handler, Class<T> clazz) {

    if (handler != null && handler instanceof HandlerMethod) {
      val hm = ((HandlerMethod) handler).getBean();

      if (clazz.isAssignableFrom(hm.getClass())) {
        return (T) hm;
      }

      return null;
    }

    return null;
  }
}
