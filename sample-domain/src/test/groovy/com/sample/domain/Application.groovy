package com.sample.domain

import com.sample.ComponentScanBasePackage
import com.sample.domain.dto.common.DefaultPageFactoryImpl
import com.sample.domain.dto.common.PageFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean

@SpringBootApplication(scanBasePackageClasses = ComponentScanBasePackage)
@TestConfiguration
class Application {

    @Bean
    PageFactory pageFactory() {
        return new DefaultPageFactoryImpl()
    }
}
