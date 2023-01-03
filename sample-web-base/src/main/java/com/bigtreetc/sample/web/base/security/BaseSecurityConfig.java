package com.bigtreetc.sample.web.base.security;

import static com.bigtreetc.sample.web.base.WebConst.*;

import com.bigtreetc.sample.web.base.security.rememberme.MultiDeviceRememberMeServices;
import com.bigtreetc.sample.web.base.security.rememberme.MultiDeviceTokenRepository;
import com.bigtreetc.sample.web.base.security.rememberme.PurgePersistentLoginTask;
import javax.sql.DataSource;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

/** 基底セキュリティコンフィグ */
public abstract class BaseSecurityConfig {

  @Value("${application.security.secureCookie:false}")
  protected Boolean secureCookie;

  @Value("${application.security.rememberMe.cookieName:rememberMe}")
  protected String rememberMeCookieName;

  @Value("${application.security.tokenValiditySeconds:86400}")
  protected Integer tokenValiditySeconds;

  @Value("${application.security.tokenPurgeSeconds:2592000}") // 30 days
  protected Integer tokenPurgeSeconds;

  @Autowired protected DataSource dataSource;

  @Autowired protected UserDetailsService userDetailsService;

  protected static final String REMEMBER_ME_KEY = "sampleRememberMeKey";

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public MultiDeviceTokenRepository multiDeviceTokenRepository() {
    val tokenRepository = new MultiDeviceTokenRepository();
    tokenRepository.setDataSource(dataSource);
    return tokenRepository;
  }

  @Bean
  public MultiDeviceRememberMeServices multiDeviceRememberMeServices() {
    val rememberMeService =
        new MultiDeviceRememberMeServices(
            REMEMBER_ME_KEY, userDetailsService, multiDeviceTokenRepository());
    rememberMeService.setParameter("rememberMe");
    rememberMeService.setCookieName(rememberMeCookieName);
    rememberMeService.setUseSecureCookie(secureCookie);
    rememberMeService.setTokenValiditySeconds(tokenValiditySeconds);
    return rememberMeService;
  }

  @Bean
  public PurgePersistentLoginTask purgePersistentLoginTask() {
    val task = new PurgePersistentLoginTask();
    task.setTokenPurgeSeconds(tokenPurgeSeconds);
    task.setDataSource(dataSource);
    return task;
  }

  @Bean
  public AccessDeniedHandler accessDeniedHandler() {
    return new DefaultAccessDeniedHandler();
  }

  @Bean
  public AuthenticationEntryPoint authenticationEntryPoint() {
    return new DefaultAuthenticationEntryPoint(LOGIN_URL, LOGIN_TIMEOUT_URL);
  }
}
