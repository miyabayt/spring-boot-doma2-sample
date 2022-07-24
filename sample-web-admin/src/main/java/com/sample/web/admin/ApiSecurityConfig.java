package com.sample.web.admin;

import com.sample.web.base.security.BaseApiSecurityConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
@Order(1)
public class ApiSecurityConfig extends BaseApiSecurityConfig {}
