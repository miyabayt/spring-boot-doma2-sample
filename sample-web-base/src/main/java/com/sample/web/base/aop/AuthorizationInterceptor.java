package com.sample.web.base.aop;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;

import com.sample.web.base.security.authorization.PermissionKeyResolver;
import com.sample.web.base.util.WebSecurityUtils;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuthorizationInterceptor extends BaseHandlerInterceptor {

    @Autowired
    PermissionKeyResolver permissionKeyResolver;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // コントローラーの動作前
        if (isRestController(handler)) {
            // APIの場合はスキップする
            return true;
        }

        val permissionKey = permissionKeyResolver.resolve(handler);

        if (permissionKey != null && !WebSecurityUtils.hasRole(permissionKey)) {
            String loginId = WebSecurityUtils.getLoginId();
            throw new AccessDeniedException(
                    "permission denied. [loginId=" + loginId + ", permissionKey=" + permissionKey + "]");
        }

        return true;
    }
}
