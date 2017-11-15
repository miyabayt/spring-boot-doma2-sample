package com.sample.web.admin;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import com.sample.web.base.security.BaseApiSecurityConfig;

@Configuration
@Order(1)
public class ApiSecurityConfig extends BaseApiSecurityConfig {

}
