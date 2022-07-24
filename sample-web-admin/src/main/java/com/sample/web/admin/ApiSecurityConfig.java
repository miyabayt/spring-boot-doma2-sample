package com.sample.web.admin;

import static com.sample.web.base.WebConst.API_BASE_URL;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

@Configuration
public class ApiSecurityConfig {

  @Autowired UserDetailsService userDetailsService;

  @Autowired PasswordEncoder passwordEncoder;

  @Bean
  @Order(2)
  public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
    val authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
    authenticationManagerBuilder
        .userDetailsService(userDetailsService)
        .passwordEncoder(passwordEncoder);
    val authenticationManager = authenticationManagerBuilder.build();

    http.antMatcher(API_BASE_URL)
        // すべてのリクエストに認証をかける
        .authorizeRequests()
        .anyRequest()
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
    return http.build();
  }
}
