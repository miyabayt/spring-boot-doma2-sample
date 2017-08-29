package com.sample.web.base.aop;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.sample.web.base.security.authorization.PermissionKeyResolver;
import com.sample.web.base.util.WebSecurityUtils;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuthorizationInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    PermissionKeyResolver permissionKeyResolver;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // コントローラーの動作前
        val permissionKey = permissionKeyResolver.resolve(handler);

        if (permissionKey != null && !WebSecurityUtils.hasRole(permissionKey)) {
            String loginId = WebSecurityUtils.getLoginId();
            throw new AccessDeniedException(
                    "permission denied. [loginId=" + loginId + ", permissionKey=" + permissionKey + "]");
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
