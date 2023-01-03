package com.bigtreetc.sample.web.base.aop;

import com.bigtreetc.sample.domain.dao.DoubleSubmitCheckTokenHolder;
import com.bigtreetc.sample.web.base.security.DoubleSubmitCheckToken;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.web.servlet.ModelAndView;

/** 二重送信防止チェックのトークンをセッションに設定する */
@Slf4j
public class SetDoubleSubmitCheckTokenInterceptor extends BaseHandlerInterceptor {

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    // コントローラーの動作前
    val expected = DoubleSubmitCheckToken.getExpectedToken(request);
    val actual = DoubleSubmitCheckToken.getActualToken(request);
    DoubleSubmitCheckTokenHolder.set(expected, actual);
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
    if (request.getMethod().equalsIgnoreCase("POST")) {
      // POSTされたときにトークンが一致していれば新たなトークンを発行する
      val expected = DoubleSubmitCheckToken.getExpectedToken(request);
      val actual = DoubleSubmitCheckToken.getActualToken(request);

      if (expected != null && actual != null && Objects.equals(expected, actual)) {
        DoubleSubmitCheckToken.renewToken(request);
      }
    }
  }

  @Override
  public void afterCompletion(
      HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
      throws Exception {
    // 処理完了後
    DoubleSubmitCheckTokenHolder.clear();
  }
}
