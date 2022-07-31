package com.sample.web.admin;

import static com.sample.web.base.WebConst.*;

import com.sample.web.base.security.BaseSecurityConfig;
import com.sample.web.base.util.RequestUtils;
import lombok.val;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableGlobalMethodSecurity(
    prePostEnabled = true,
    securedEnabled = true) // アノテーションでロール、権限チェックを行うために定義する
@Configuration
@EnableWebSecurity
public class SecurityConfig extends BaseSecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http, PasswordEncoder passwordEncoder)
      throws Exception {
    val authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
    authenticationManagerBuilder
        .userDetailsService(userDetailsService)
        .passwordEncoder(passwordEncoder);
    val authenticationManager = authenticationManagerBuilder.build();

    // CookieにCSRFトークンを保存する
    http.csrf().csrfTokenRepository(new CookieCsrfTokenRepository());

    String[] permittedUrls = {
      LOGIN_TIMEOUT_URL,
      FORBIDDEN_URL,
      ERROR_URL,
      RESET_PASSWORD_URL,
      CHANGE_PASSWORD_URL,
      WEBJARS_URL,
      STATIC_RESOURCES_URL
    };

    http.authorizeRequests()
        .antMatchers(API_BASE_URL)
        .authenticated()
        // Basic認証をかける
        .and()
        .authenticationManager(authenticationManager)
        .httpBasic()
        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
        // CSRFチェックをしない
        .and()
        .csrf()
        .disable();

    http.authorizeRequests()
        // エラー画面は認証をかけない
        .antMatchers(permittedUrls)
        .permitAll()
        // エラー画面以外は、認証をかける
        .anyRequest()
        .authenticated()
        .and()
        .authenticationManager(authenticationManager)
        .exceptionHandling()
        .authenticationEntryPoint(authenticationEntryPoint())
        .accessDeniedHandler(accessDeniedHandler());

    http.formLogin()
        // ログイン画面のURL
        .loginPage(LOGIN_URL)
        // 認可を処理するURL
        .loginProcessingUrl(LOGIN_PROCESSING_URL)
        // ログイン成功時の遷移先
        .successForwardUrl(LOGIN_SUCCESS_URL)
        // ログイン失敗時の遷移先
        .failureUrl(LOGIN_FAILURE_URL)
        // ログインIDのパラメータ名
        .usernameParameter("loginId")
        // パスワードのパラメータ名
        .passwordParameter("password")
        .permitAll();

    // ログアウト設定
    http.logout() //
        .logoutRequestMatcher(new AntPathRequestMatcher(LOGOUT_URL))
        // Cookieを破棄する
        .deleteCookies("SESSION", "JSESSIONID", rememberMeCookieName)
        // ログアウト画面のURL
        .logoutUrl(LOGOUT_URL)
        // ログアウト後の遷移先
        .logoutSuccessUrl(LOGOUT_SUCCESS_URL)
        // ajaxの場合は、HTTPステータスを返す
        .defaultLogoutSuccessHandlerFor(
            new HttpStatusReturningLogoutSuccessHandler(), RequestUtils::isAjaxRequest)
        // セッションを破棄する
        .invalidateHttpSession(true)
        .permitAll();

    // RememberMe
    http.rememberMe().key(REMEMBER_ME_KEY).rememberMeServices(multiDeviceRememberMeServices());

    return http.build();
  }
}
