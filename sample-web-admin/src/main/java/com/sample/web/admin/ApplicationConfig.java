package com.sample.web.admin;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.sample.web.base.BaseApplicationConfig;
import org.springframework.web.bind.annotation.GetMapping;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableCaching // JCacheを有効可する
@EnableSwagger2 //Swaggerを有効にする
public class ApplicationConfig extends BaseApplicationConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("api")
                .select()
                .paths(PathSelectors.regex("/api.*"))
                .build()
                .apiInfo(apiinfo());
    }

    @Bean
    public Docket admin() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("admin")
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .apiInfo(admininfo());
    }

    private ApiInfo apiinfo() {
        return new ApiInfoBuilder()
                .title("REST API List")
                .description("REST APIの一覧です。")
                .version("1.0")
                .contact(new Contact("miyabayt","https://github.com/miyabayt/spring-boot-doma2-sample/issues/", "architecture@bigtreetc.com"))
                .build();
    }

    private ApiInfo admininfo() {
        return new ApiInfoBuilder()
                .title("ALL API List")
                .description("全てのAPIの一覧です。")
                .version("1.0")
                .contact(new Contact("miyabayt","https://github.com/miyabayt/spring-boot-doma2-sample/issues/", "architecture@bigtreetc.com"))
                .build();
    }
}
