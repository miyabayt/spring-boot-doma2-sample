package com.sample.web.front;

import java.util.Arrays;
import java.util.List;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

import com.sample.web.base.BaseApplicationConfig;

@Configuration
@EnableCaching // JCacheを有効可する
public class ApplicationConfig extends BaseApplicationConfig {

    @Override
    protected List<String> getCorsAllowedOrigins() {
        return Arrays.asList("*");
    }
}
