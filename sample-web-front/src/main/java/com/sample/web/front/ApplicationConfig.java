package com.sample.web.front;

import java.util.Arrays;
import java.util.List;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import com.ulisesbocchio.jasyptspringboot.annotation.EncryptablePropertySource;
import com.ulisesbocchio.jasyptspringboot.annotation.EncryptablePropertySources;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

import com.sample.web.base.BaseApplicationConfig;

@Configuration
@EnableCaching // JCacheを有効化する
@EnableEncryptableProperties //Passwordを暗号化する
@EncryptablePropertySources({@EncryptablePropertySource("classpath:application-development.yml"),
                                @EncryptablePropertySource("classpath:application-staging.yml")})
public class ApplicationConfig extends BaseApplicationConfig {

    @Override
    protected List<String> getCorsAllowedOrigins() {
        return Arrays.asList("*");
    }
}
