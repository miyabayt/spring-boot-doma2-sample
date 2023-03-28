package com.bigtreetc.sample.batch;

import com.bigtreetc.sample.common.util.MessageUtils;
import com.bigtreetc.sample.domain.DefaultModelMapperFactory;
import lombok.val;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
public class BatchConfig {

  @Value("${application.batch.corePoolSize:2}")
  int corePoolSize = 2;

  @Value("${application.batch.maxPoolSize:64}")
  int maxPoolSize = 64;

  @Autowired
  public void initUtils(MessageSource messageSource) {
    MessageUtils.init(messageSource);
  }

  @Bean
  public ThreadPoolTaskExecutor taskExecutor() {
    val executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(corePoolSize);
    executor.setMaxPoolSize(maxPoolSize);
    executor.setWaitForTasksToCompleteOnShutdown(true);
    return executor;
  }

  @Bean
  public ModelMapper modelMapper() {
    // ObjectMappingのためのマッパー
    return DefaultModelMapperFactory.create();
  }

  @Bean
  public LocalValidatorFactoryBean beanValidator(MessageSource messageSource) {
    val bean = new LocalValidatorFactoryBean();
    bean.setValidationMessageSource(messageSource);
    return bean;
  }
}
