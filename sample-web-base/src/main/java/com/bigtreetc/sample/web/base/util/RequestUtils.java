package com.bigtreetc.sample.web.base.util;

import jakarta.servlet.http.HttpServletRequest;
import lombok.val;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

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
    return request.getHeader(HttpHeaders.USER_AGENT);
  }

  /**
   * Ajax通信であるかを示す値を返します。
   *
   * @param request
   * @return
   */
  public static boolean isAjaxRequest(HttpServletRequest request) {
    val header = request.getHeader(X_REQUESTED_WITH);
    return XMLHTTP_REQUEST.equalsIgnoreCase(header);
  }

  /**
   * HttpServletRequestを返します。
   *
   * @return
   */
  public static HttpServletRequest getHttpServletRequest() {
    val attributes = RequestContextHolder.getRequestAttributes();
    return ((ServletRequestAttributes) attributes).getRequest();
  }

  /**
   * サイトURLを返します。
   *
   * @return
   */
  public static String getSiteUrl() {
    val servletRequest = getHttpServletRequest();

    String scheme = servletRequest.getScheme();
    String host = servletRequest.getRemoteHost();
    int port = servletRequest.getServerPort();
    String path = servletRequest.getContextPath();

    String siteUrl = null;
    if (port == 80 || port == 443) {
      siteUrl = String.join("", scheme, "://", host, path);
    } else {
      siteUrl = String.join("", scheme, "://", host, ":", String.valueOf(port), path);
    }

    return siteUrl;
  }
}
