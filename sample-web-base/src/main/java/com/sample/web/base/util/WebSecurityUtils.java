package com.sample.web.base.util;

import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.val;

public class WebSecurityUtils {

    private static final SpelParserConfiguration config = new SpelParserConfiguration(true, true);

    private static final SpelExpressionParser parser = new SpelExpressionParser(config);

    /**
     * 認証情報を取得します。
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T getPrincipal() {
        val auth = SecurityContextHolder.getContext().getAuthentication();
        return (T) auth.getPrincipal();
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

        boolean isAllowed = false;
        for (GrantedAuthority ga : authorities) {
            val authority = ga.getAuthority();
            val expressionString = String.format("'%s' matches '%s'", role, authority);
            val expression = parser.parseExpression(expressionString);

            isAllowed = expression.getValue(Boolean.class);
            if (isAllowed) {
                break;
            }
        }

        return isAllowed;
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
