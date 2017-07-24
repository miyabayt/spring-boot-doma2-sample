package com.sample.web.front;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

import com.sample.web.base.security.BaseSecurityConfig;

@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true) // アノテーションでロール
@Configuration
public class SecurityConfig extends BaseSecurityConfig {

}
