package com.sample.web.front;

import com.sample.web.base.BaseApplicationConfig;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching // JCacheを有効化する
public class ApplicationConfig extends BaseApplicationConfig {}
