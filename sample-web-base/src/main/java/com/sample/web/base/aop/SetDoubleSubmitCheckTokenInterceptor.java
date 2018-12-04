package com.sample.web.base.aop;

import java.util.Objects;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sample.web.base.security.annotation.TokenKey;
import com.sample.web.base.security.annotation.ExcludeCheckToken;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;

import com.sample.domain.dao.DoubleSubmitCheckTokenHolder;
import com.sample.web.base.security.DoubleSubmitCheckToken;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

/**
 * 二重送信防止チェックのトークンをセッションに設定する
 */
@Slf4j
public class SetDoubleSubmitCheckTokenInterceptor extends BaseHandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // コントローラーの動作前
        val key = takeOutTokenKey(handler).orElse(null);
        val expected = DoubleSubmitCheckToken.getExpectedToken(request, key);
        val actual = DoubleSubmitCheckToken.getActualToken(request);
        val excludeCheck = excludeCheckToken(handler);
        DoubleSubmitCheckTokenHolder.set(key, expected, actual, excludeCheck);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        // コントローラーの動作後
        if (StringUtils.equalsIgnoreCase(request.getMethod(), "POST")) {
            if(DoubleSubmitCheckTokenHolder.isExcludeCheck()){
                return;
            }
            // POSTされたときにトークンが一致していれば新たなトークンを発行する
            val key = DoubleSubmitCheckTokenHolder.getTokenKey();
            val expected = DoubleSubmitCheckToken.getExpectedToken(request, key);
            val actual = DoubleSubmitCheckToken.getActualToken(request);

            if (expected != null && actual != null && Objects.equals(expected, actual)) {
                DoubleSubmitCheckToken.renewToken(request, key);
            }
        }
    }

    boolean excludeCheckToken(Object handler) {
        if (!(handler instanceof HandlerMethod)) {
            return false;
        }
        val hm = (HandlerMethod)handler;
        if (hm.getBeanType().isAnnotationPresent(ExcludeCheckToken.class)
                || hm.getMethod().isAnnotationPresent(ExcludeCheckToken.class)) {
            return true;
        }
        return false;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        // 処理完了後
        DoubleSubmitCheckTokenHolder.clear();
    }

    Optional<String> takeOutTokenKey(Object handler) {
        if (!(handler instanceof HandlerMethod)) {
            return Optional.empty();
        }
        val hm = (HandlerMethod)handler;
        if(hm.getMethod().isAnnotationPresent(TokenKey.class)){
            return Optional.of(hm.getMethod().getAnnotation(TokenKey.class).value());
        }
        if(hm.getBeanType().isAnnotationPresent(TokenKey.class)){
            return Optional.of(hm.getBeanType().getAnnotation(TokenKey.class).value());
        }
        return Optional.empty();
    }

}
