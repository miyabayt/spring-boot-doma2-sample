package com.sample.web.front;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

import com.sample.web.base.BaseApplicationConfig;

@Configuration
@EnableCaching // JCacheを有効化する
public class ApplicationConfig extends BaseApplicationConfig {

}
