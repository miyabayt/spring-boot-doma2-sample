package com.sample.web.base;

import com.sample.web.base.security.DoubleSubmitCheckingRequestDataValueProcessor;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.support.RequestDataValueProcessor;

/**
 * CsrfRequestDataValueProcessorと自前のRequestDataValueProcessorを共存させるための設定<br>
 * META-INF/spring.factoriesに本クラス名を記述する
 */
@Configuration
@AutoConfigureAfter(SecurityAutoConfiguration.class)
public class RequestDataValueProcessorAutoConfiguration {

  // requestDataValueProcessorという名称でなければならない
  @Bean
  public RequestDataValueProcessor requestDataValueProcessor() {
    // 二重送信防止のトークンを自動で埋め込む
    return new DoubleSubmitCheckingRequestDataValueProcessor();
  }
}
