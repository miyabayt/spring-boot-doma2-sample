package com.bigtreetc.sample.web.admin;

import com.bigtreetc.sample.web.base.BaseApplicationConfig;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching // JCacheを有効可する
public class ApplicationConfig extends BaseApplicationConfig {}
