package com.bigtreetc.sample.domain

import com.bigtreetc.sample.ComponentScanBasePackage
import org.modelmapper.ModelMapper
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean

@EnableCaching
@SpringBootApplication(scanBasePackageClasses = ComponentScanBasePackage)
@TestConfiguration
class Application {

    @Bean
    ModelMapper modelMapper() {
        // ObjectMappingのためのマッパー
        return DefaultModelMapperFactory.create()
    }
}
