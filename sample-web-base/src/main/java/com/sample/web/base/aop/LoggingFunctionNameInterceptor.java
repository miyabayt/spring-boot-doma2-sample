package com.sample.web.base.aop;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.MDC;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.sample.common.FunctionNameAware;
import com.sample.web.base.WebConst;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

/**
 * 機能名をログに出力する
 */
@Slf4j
public class LoggingFunctionNameInterceptor extends HandlerInterceptorAdapter {

    private static final String MDC_FUNCTION_NAME = WebConst.MDC_FUNCTION_NAME;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // コントローラーの動作前

        if (handler != null && handler instanceof HandlerMethod) {
            if (((HandlerMethod) handler).getBean() instanceof FunctionNameAware) {
                val functionName = ((FunctionNameAware) ((HandlerMethod) handler).getBean()).getFunctionName();
                MDC.put(MDC_FUNCTION_NAME, functionName);
            }
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        // コントローラーの動作後
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        // 処理完了後
    }
}
