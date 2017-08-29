package com.sample.web.base.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.val;

public class WebSecurityUtils {

    /**
     * 認証情報を取得します。
     * 
     * @return
     */
    public static Object getPrincipal() {
        val auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getPrincipal();
    }

    /**
     * 引数に指定した権限を持っているかどうかを示す値を返します。
     * 
     * @param role
     * @return
     */
    public static boolean hasRole(final String role) {
        val auth = SecurityContextHolder.getContext().getAuthentication();
        val authorities = auth.getAuthorities();

        boolean isPermittedAll = authorities.stream().anyMatch(a -> "*".equals(a.getAuthority()));
        if (isPermittedAll) {
            return true;
        }

        return authorities.stream().anyMatch(a -> StringUtils.equalsIgnoreCase(role, a.getAuthority()));
    }

    /**
     * ログインIDを取得する。
     * 
     * @return
     */
    public static String getLoginId() {
        String loginId = null;
        val principal = WebSecurityUtils.getPrincipal();

        if (principal instanceof UserDetails) {
            loginId = ((UserDetails) principal).getUsername();
        }

        return loginId;
    }
}
