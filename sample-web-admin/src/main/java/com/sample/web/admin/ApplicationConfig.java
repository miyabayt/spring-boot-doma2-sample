package com.sample.web.admin;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

import com.sample.web.base.BaseApplicationConfig;

@Configuration
@EnableCaching // JCacheを有効可する
public class ApplicationConfig extends BaseApplicationConfig {

}
