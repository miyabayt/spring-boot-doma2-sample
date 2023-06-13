package com.bigtreetc.sample.web.admin;

import com.bigtreetc.sample.web.base.WebConst;
import com.bigtreetc.sample.web.base.security.BaseSecurityConfig;
import com.bigtreetc.sample.web.base.security.CorsProperties;
import com.bigtreetc.sample.web.base.util.RequestUtils;
import lombok.val;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableMethodSecurity // アノテーションでロール、権限チェックを行うために定義する
@Configuration
@EnableConfigurationProperties({CorsProperties.class})
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
    http.csrf(csrf -> csrf.csrfTokenRepository(new CookieCsrfTokenRepository()));

    String[] permittedUrls = {
      WebConst.LOGIN_TIMEOUT_URL,
      WebConst.FORBIDDEN_URL,
      WebConst.ERROR_URL,
      WebConst.RESET_PASSWORD_URL,
      WebConst.CHANGE_PASSWORD_URL,
      WebConst.WEBJARS_URL,
      WebConst.STATIC_RESOURCES_URL
    };

    http.authorizeHttpRequests(
        authorize -> {
          // エラー画面は認証をかけない
          authorize
              .requestMatchers(permittedUrls)
              .permitAll()
              // エラー画面以外は、認証をかける
              .anyRequest()
              .authenticated();
        });

    http.authenticationManager(authenticationManager)
        .exceptionHandling(
            exceptionHandling ->
                exceptionHandling
                    .authenticationEntryPoint(authenticationEntryPoint())
                    .accessDeniedHandler(accessDeniedHandler()));

    http.formLogin(
        formLogin -> {
          // ログイン画面のURL
          formLogin
              .loginPage(WebConst.LOGIN_URL)
              // 認可を処理するURL
              .loginProcessingUrl(WebConst.LOGIN_PROCESSING_URL)
              // ログイン成功時の遷移先
              .successForwardUrl(WebConst.LOGIN_SUCCESS_URL)
              // ログイン失敗時の遷移先
              .failureUrl(WebConst.LOGIN_FAILURE_URL)
              // ログインIDのパラメータ名
              .usernameParameter("loginId")
              // パスワードのパラメータ名
              .passwordParameter("password")
              .permitAll();
        });

    // ログアウト設定
    http.logout(
        logout ->
            logout
                .logoutRequestMatcher(new AntPathRequestMatcher(WebConst.LOGOUT_URL))
                // Cookieを破棄する
                .deleteCookies("SESSION", "JSESSIONID", rememberMeCookieName)
                // ログアウト画面のURL
                .logoutUrl(WebConst.LOGOUT_URL)
                // ログアウト後の遷移先
                .logoutSuccessUrl(WebConst.LOGOUT_SUCCESS_URL)
                // ajaxの場合は、HTTPステータスを返す
                .defaultLogoutSuccessHandlerFor(
                    new HttpStatusReturningLogoutSuccessHandler(), RequestUtils::isAjaxRequest)
                // セッションを破棄する
                .invalidateHttpSession(true)
                .permitAll());

    // RememberMe
    http.rememberMe(
        rememberMe ->
            rememberMe.key(REMEMBER_ME_KEY).rememberMeServices(multiDeviceRememberMeServices()));

    return http.build();
  }
}
