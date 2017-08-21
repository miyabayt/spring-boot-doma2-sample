package com.sample.web.base.util;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;

import lombok.val;

public class RequestUtils {

    public static final String X_REQUESTED_WITH = "X-Requested-With";

    public static final String XMLHTTP_REQUEST = "XMLHttpRequest";

    /**
     * User-Agentを取得します。
     * 
     * @param request
     * @return
     */
    public static String getUserAgent(HttpServletRequest request) {
        return StringUtils.trimToEmpty(request.getParameter(HttpHeaders.USER_AGENT));
    }

    /**
     * Ajax通信であるかを示す値を返します。
     * 
     * @param request
     * @return
     */
    public static boolean isAjaxRequest(HttpServletRequest request) {
        val header = request.getHeader(X_REQUESTED_WITH);
        val isAjax = StringUtils.equalsIgnoreCase(XMLHTTP_REQUEST, header);
        return isAjax;
    }
}
