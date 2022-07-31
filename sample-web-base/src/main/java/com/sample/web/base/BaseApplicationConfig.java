package com.sample.web.base;

import static com.sample.web.base.WebConst.*;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.sample.common.util.MessageUtils;
import com.sample.domain.DefaultModelMapperFactory;
import com.sample.domain.dto.system.CodeCategoryCriteria;
import com.sample.domain.dto.system.CodeCriteria;
import com.sample.domain.repository.system.CodeCategoryRepository;
import com.sample.domain.repository.system.CodeRepository;
import com.sample.web.base.aop.*;
import com.sample.web.base.controller.LocalDateConverter;
import com.sample.web.base.controller.LocalDateTimeConverter;
import com.sample.web.base.controller.api.resource.DefaultResourceFactoryImpl;
import com.sample.web.base.controller.api.resource.ResourceFactory;
import com.sample.web.base.filter.CheckOverflowFilter;
import com.sample.web.base.filter.ClearMDCFilter;
import com.sample.web.base.filter.LoginUserTrackingFilter;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.val;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.data.domain.Pageable;
import org.springframework.format.FormatterRegistry;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.filter.ForwardedHeaderFilter;
import org.springframework.web.filter.ShallowEtagHeaderFilter;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

/** 基底アプリケーション設定 */
public abstract class BaseApplicationConfig
    implements WebServerFactoryCustomizer<ConfigurableServletWebServerFactory>, WebMvcConfigurer {

  @Value("${application.cors.allowCredentials:true}")
  Boolean allowCredentials;

  @Value("#{'${application.cors.allowedHeaders:*}'.split(',')}")
  List<String> allowedHeaders;

  @Value("#{'${application.cors.allowedMethods:*}'.split(',')}")
  List<String> allowedMethods;

  @Value("#{'${application.cors.allowedOrigins:*}'.split(',')}")
  List<String> corsAllowedOrigins;

  @Value("${application.cors.maxAge:86400}")
  Long maxAge;

  @Autowired
  public void initUtils(MessageSource messageSource) {
    MessageUtils.init(messageSource);
  }

  @Override
  public void customize(ConfigurableServletWebServerFactory container) {}

  @Override
  public void addViewControllers(ViewControllerRegistry registry) {
    // コントローラーを追加する
    registry.addViewController(FORBIDDEN_URL).setViewName(FORBIDDEN_VIEW);
    registry.addViewController(ERROR_URL).setViewName(ERROR_VIEW);
    registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
  }

  @Override
  public void addFormatters(FormatterRegistry registry) {
    registry.addConverter(new LocalDateConverter(LOCALDATE_FORMAT));
    registry.addConverter(new LocalDateTimeConverter(LOCALDATETIME_FORMAT));
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(localeChangeInterceptor());
    registry.addInterceptor(requestTrackingInterceptor());
    registry.addInterceptor(loggingFunctionNameInterceptor());
    registry.addInterceptor(setAuditInfoInterceptor());
    registry.addInterceptor(setDoubleSubmitCheckTokenInterceptor());
    registry.addInterceptor(setModelAndViewInterceptor());
  }

  @Bean
  public ForwardedHeaderFilter forwardedHeaderFilter() {
    // X-Forwarded-XXXの値を使ってリクエスト情報を上書きする
    return new ForwardedHeaderFilter();
  }

  @Bean
  public ShallowEtagHeaderFilter shallowEtagHeaderFilter() {
    // ETagの制御を行う
    return new ShallowEtagHeaderFilter();
  }

  @Bean
  public FilterRegistrationBean<CorsFilter> corsFilter() {
    val config = new CorsConfiguration();
    config.setAllowCredentials(allowCredentials);
    config.setAllowedHeaders(allowedHeaders);
    config.setAllowedOrigins(corsAllowedOrigins);
    config.setAllowedMethods(allowedMethods);
    config.setMaxAge(maxAge);

    val source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);

    val bean = new FilterRegistrationBean<CorsFilter>(new CorsFilter(source));
    bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
    return bean;
  }

  @Bean
  public FilterRegistrationBean<LoginUserTrackingFilter> loginUserTrackingFilterBean() {
    String[] excludeUrlPatterns = {WEBJARS_URL, STATIC_RESOURCES_URL};
    val filter = new LoginUserTrackingFilter(excludeUrlPatterns);
    val bean = new FilterRegistrationBean<>(filter);
    bean.setOrder(Ordered.LOWEST_PRECEDENCE);
    return bean;
  }

  @Bean
  public FilterRegistrationBean<ClearMDCFilter> clearMDCFilterBean() {
    val filter = new ClearMDCFilter();
    val bean = new FilterRegistrationBean<>(filter);
    bean.setOrder(Ordered.LOWEST_PRECEDENCE);
    return bean;
  }

  @Bean
  public FilterRegistrationBean<CheckOverflowFilter> checkOverflowFilterBean() {
    val filter = new CheckOverflowFilter();
    val bean = new FilterRegistrationBean<>(filter);
    bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
    return bean;
  }

  @Bean
  public ModelMapper modelMapper() {
    // ObjectMappingのためのマッパー
    return DefaultModelMapperFactory.create();
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
  public CacheManager cacheManager(
      CodeCategoryRepository codeCategoryRepository, CodeRepository codeRepository) {
    val codeCategoryCache =
        new CaffeineCache(
            "codeCategoryCache",
            Caffeine.newBuilder()
                .refreshAfterWrite(5, TimeUnit.MINUTES)
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .build(
                    (key) -> {
                      val criteria = new CodeCategoryCriteria();
                      criteria.setCategoryCode((String) key);
                      return codeCategoryRepository
                          .findAll(criteria, Pageable.unpaged())
                          .getContent();
                    }));
    val codeCache =
        new CaffeineCache(
            "codeCache",
            Caffeine.newBuilder()
                .refreshAfterWrite(5, TimeUnit.MINUTES)
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .build(
                    (key) -> {
                      val criteria = new CodeCriteria();
                      criteria.setCategoryCode((String) key);
                      return codeRepository.findAll(criteria, Pageable.unpaged()).getContent();
                    }));
    val manager = new SimpleCacheManager();
    manager.setCaches(List.of(codeCategoryCache, codeCache));
    return manager;
  }

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    // webjarsをResourceHandlerに登録する
    registry
        .addResourceHandler(WEBJARS_URL)
        // JARの中身をリソースロケーションにする
        .addResourceLocations("classpath:/META-INF/resources/webjars/")
        // webjars-locatorを使うためにリソースチェイン内のキャッシュを無効化する
        .resourceChain(false);
  }

  @Bean
  public RequestTrackingInterceptor requestTrackingInterceptor() {
    // MDCにIDを設定してリクエストをトラッキングする
    String[] excludeUrlPatterns = {WEBJARS_URL, STATIC_RESOURCES_URL};
    return new RequestTrackingInterceptor(excludeUrlPatterns);
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

  @Bean
  public SetModelAndViewInterceptor setModelAndViewInterceptor() {
    // 共通的な定数定義などを画面変数に設定する
    return new SetModelAndViewInterceptor();
  }

  @Bean
  public ResourceFactory resourceFactory() {
    return new DefaultResourceFactoryImpl();
  }
}
