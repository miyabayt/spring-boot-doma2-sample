package com.bigtreetc.sample.domain

import com.bigtreetc.sample.ComponentScanBasePackage

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.cache.annotation.EnableCaching

@EnableCaching
@SpringBootApplication(scanBasePackageClasses = ComponentScanBasePackage)
@TestConfiguration
class Application {


}
