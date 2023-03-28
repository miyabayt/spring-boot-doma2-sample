package com.bigtreetc.sample.web.base.filter;

import static com.bigtreetc.sample.web.base.WebConst.MDC_LOGIN_USER_ID;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.slf4j.MDC;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

/** ログインIDをMDCに設定する */
@Slf4j
public class LoginUserTrackingFilter extends OncePerRequestFilter {

  private static final AntPathMatcher pathMatcher = new AntPathMatcher();

  private final String[] excludeUrlPatterns;

  public LoginUserTrackingFilter(String[] excludeUrlPatterns) {
    this.excludeUrlPatterns = excludeUrlPatterns;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    try {
      if (!shouldNotFilter(request)) {
        MDC.put(MDC_LOGIN_USER_ID, "GUEST");
        getUserId().ifPresent(userId -> MDC.put(MDC_LOGIN_USER_ID, userId));
      }
    } finally {
      filterChain.doFilter(request, response);
    }
  }

  protected Optional<String> getUserId() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication != null) {
      Object principal = authentication.getPrincipal();

      if (principal instanceof UserIdAware) {
        val loginId = ((UserIdAware) principal).getUserId();
        return Optional.of(loginId);
      }
    }

    return Optional.empty();
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    for (val excludeUrlPattern : excludeUrlPatterns) {
      if (pathMatcher.match(excludeUrlPattern, request.getServletPath())) {
        return true;
      }
    }

    return false;
  }
}
