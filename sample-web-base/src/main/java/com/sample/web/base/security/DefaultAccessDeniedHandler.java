package com.sample.web.base.security;

import static com.sample.web.base.WebConst.FORBIDDEN_URL;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.csrf.MissingCsrfTokenException;

public class DefaultAccessDeniedHandler implements AccessDeniedHandler {

    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Autowired
    AuthenticationEntryPoint authenticationEntryPoint;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException, ServletException {

        if (response.isCommitted()) {
            return;
        }

        if (accessDeniedException instanceof MissingCsrfTokenException) {
            authenticationEntryPoint.commence(request, response, null);
        } else {
            redirectStrategy.sendRedirect(request, response, FORBIDDEN_URL);
        }
    }
}
