package com.bigtreetc.sample.web.api;

import com.bigtreetc.sample.web.api.base.resource.DefaultResourceFactoryImpl;
import com.bigtreetc.sample.web.api.base.resource.ResourceFactory;
import com.bigtreetc.sample.web.base.BaseApplicationConfig;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching // JCacheを有効化する
public class ApplicationConfig extends BaseApplicationConfig {

  @Bean
  public ResourceFactory resourceFactory() {
    return new DefaultResourceFactoryImpl();
  }
}
