package com.sample.web.base.aop;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.sample.web.base.controller.api.AbstractRestController;

import lombok.val;

/**
 * 基底インターセプター
 */
public abstract class BaseHandlerInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // コントローラーの動作前
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

    /**
     * RestControllerであるかどうかを示す値を返します。
     * 
     * @param handler
     * @return
     */
    protected boolean isRestController(Object handler) {
        val bean = getBean(handler, AbstractRestController.class);

        if (bean instanceof AbstractRestController) {
            return true;
        }

        return false;
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
