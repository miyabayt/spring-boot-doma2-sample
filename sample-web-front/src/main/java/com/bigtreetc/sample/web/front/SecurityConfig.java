package com.bigtreetc.sample.web.front;

import com.bigtreetc.sample.web.base.security.BaseSecurityConfig;
import com.bigtreetc.sample.web.base.security.CorsProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableConfigurationProperties({CorsProperties.class})
@EnableWebSecurity
public class SecurityConfig extends BaseSecurityConfig {}
