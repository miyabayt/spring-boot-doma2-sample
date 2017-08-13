package com.sample.web.base;

import static com.sample.web.base.WebConst.*;

import java.util.Arrays;
import java.util.List;

import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.PropertyInfo;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.filter.ForwardedHeaderFilter;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.filter.ShallowEtagHeaderFilter;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import com.sample.domain.dto.common.DomaDto;
import com.sample.domain.dto.common.ID;
import com.sample.web.base.aop.LoggingFunctionNameInterceptor;
import com.sample.web.base.aop.RequestTrackingInterceptor;
import com.sample.web.base.aop.SetAuditInfoInterceptor;
import com.sample.web.base.aop.SetDoubleSubmitCheckTokenInterceptor;
import com.sample.web.base.filter.ClearMDCFilter;
import com.sample.web.base.filter.CustomCharacterEncodingFilter;
import com.sample.web.base.filter.LoginUserTrackingFilter;
import com.sample.web.base.helper.DeviceHelper;

import lombok.val;

/**
 * 基底アプリケーション設定
 */
public abstract class BaseApplicationConfig extends WebMvcConfigurerAdapter
        implements EmbeddedServletContainerCustomizer {

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        // 'Accept'リクエストヘッダを無視する
        configurer.ignoreAcceptHeader(true)
                // デフォルトはHTML形式でレスポンスする
                .defaultContentType(MediaType.TEXT_HTML);
    }

    @Override
    public void customize(ConfigurableEmbeddedServletContainer container) {
        container.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, NOTFOUND_URL));
        container.addErrorPages(new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, ERROR_URL));
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // コントローラーを追加する
        registry.addViewController(NOTFOUND_URL).setViewName(NOTFOUND_VIEW);
        registry.addViewController(FORBIDDEN_URL).setViewName(FORBIDDEN_VIEW);
        registry.addViewController(ERROR_URL).setViewName(ERROR_VIEW);
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
    }

    @Bean
    public ForwardedHeaderFilter forwardedHeaderFilter() {
        // X-Forwarded-XXXの値を使ってリクエスト情報を上書きする
        return new ForwardedHeaderFilter();
    }

    @Bean
    public HiddenHttpMethodFilter hiddenHttpMethodFilter() {
        // hiddenパラメータで指定されたHTTPメソッドに変換する
        return new HiddenHttpMethodFilter();
    }

    @Bean
    public ShallowEtagHeaderFilter shallowEtagHeaderFilter() {
        // ETagの制御を行う
        return new ShallowEtagHeaderFilter();
    }

    @Bean
    public DeviceHelper deviceHelper() {
        // デバイス判定ヘルパーをCustomCharacterEncodingFilterで使用する
        return new DeviceHelper();
    }

    @Bean
    public CustomCharacterEncodingFilter characterEncodingFilter() {
        val filter = new CustomCharacterEncodingFilter();
        filter.setCallback((request, customCharacterEncodingFilter) -> {
            // ガラケーでは文字コードをSJISに変換する
            if (deviceHelper().determineDevice(request) == DeviceHelper.DEVICE.FP) {
                customCharacterEncodingFilter.setEncoding("Shift_JIS");
            }
        });
        return filter;
    }

    @Bean
    public FilterRegistrationBean corsFilter() {
        val config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(getCorsAllowedOrigins());
        config.setMaxAge(86400L);

        val allowedHeaders = Arrays.asList("Origin", "X-Requested-With", "Content-Type", "Accept", "X-XSRF-TOKEN");
        config.setAllowedHeaders(allowedHeaders);

        val allowedMethods = Arrays.asList("POST", "GET", "PUT", "OPTIONS", "DELETE");
        config.setAllowedMethods(allowedMethods);

        val source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        val bean = new FilterRegistrationBean(new CorsFilter(source));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }

    protected abstract List<String> getCorsAllowedOrigins();

    @Bean
    public FilterRegistrationBean loginUserTrackingFilterBean() {
        val filter = new LoginUserTrackingFilter();
        filter.setExcludeUrlPatterns(Arrays.asList(WEBJARS_URL, STATIC_RESOURCES_URL));

        val bean = new FilterRegistrationBean(filter);
        bean.setOrder(Ordered.LOWEST_PRECEDENCE);
        return bean;
    }

    @Bean
    public FilterRegistrationBean clearMDCFilterBean() {
        val filter = new ClearMDCFilter();
        val bean = new FilterRegistrationBean(filter);
        bean.setOrder(Ordered.LOWEST_PRECEDENCE);
        return bean;
    }

    @Bean
    public ModelMapper modelMapper() {
        // ObjectMappingのためのマッパー
        val modelMapper = new ModelMapper();

        modelMapper.getConfiguration().setPropertyCondition(
                // IDフィールド以外をマッピングする
                context -> {
                    // DomaDtoのIDカラムは上書きしないようにする
                    PropertyInfo propertyInfo = context.getMapping().getLastDestinationProperty();
                    return !(context.getParent().getDestination() instanceof DomaDto
                            && propertyInfo.getName().equals("id"));
                });

        // コンバーター
        Converter<ID<?>, Integer> idToInt = new AbstractConverter<ID<?>, Integer>() {
            @Override
            protected Integer convert(ID<?> source) {
                return source == null ? null : source.getValue();
            }
        };

        modelMapper.addConverter(idToInt);

        return modelMapper;
    }

    @Bean
    public LocaleResolver localeResolver() {
        // Cookieに言語を保存する
        val resolver = new CookieLocaleResolver();
        resolver.setCookieName("lang");
        return resolver;
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        // langパラメータでロケールを切り替える
        val interceptor = new LocaleChangeInterceptor();
        interceptor.setParamName("lang");
        return interceptor;
    }

    @Bean
    public LocalValidatorFactoryBean beanValidator(MessageSource messageSource) {
        val bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource);
        return bean;
    }

    @Bean
    public CacheManager cacheManager() {
        val manager = new EhCacheCacheManager();
        manager.setCacheManager(ehcache().getObject());
        return manager;
    }

    @Bean
    public EhCacheManagerFactoryBean ehcache() {
        val ehcache = new EhCacheManagerFactoryBean();
        ehcache.setConfigLocation(new ClassPathResource("ehcache.xml"));
        return ehcache;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // webjarsをResourceHandlerに登録する
        registry.addResourceHandler(WEBJARS_URL)
                // JARの中身をリソースロケーションにする
                .addResourceLocations("classpath:/META-INF/resources/webjars/")
                // webjars-locatorを使うためにリソースチェイン内のキャッシュを無効化する
                .resourceChain(false);
    }

    @Bean
    public RequestTrackingInterceptor requestTrackingInterceptor() {
        // MDCにIDを設定してリクエストをトラッキングする
        return new RequestTrackingInterceptor();
    }

    @Bean
    public LoggingFunctionNameInterceptor loggingFunctionNameInterceptor() {
        // MDCに機能名を設定してログに出力する
        return new LoggingFunctionNameInterceptor();
    }

    @Bean
    public SetAuditInfoInterceptor setAuditInfoInterceptor() {
        // システム制御項目を保存してDB保存時に利用する
        return new SetAuditInfoInterceptor();
    }

    @Bean
    public SetDoubleSubmitCheckTokenInterceptor setDoubleSubmitCheckTokenInterceptor() {
        // 二重送信をチェックする
        return new SetDoubleSubmitCheckTokenInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
        registry.addInterceptor(requestTrackingInterceptor());
        registry.addInterceptor(loggingFunctionNameInterceptor());
        registry.addInterceptor(setAuditInfoInterceptor());
        registry.addInterceptor(setDoubleSubmitCheckTokenInterceptor());
    }
}
