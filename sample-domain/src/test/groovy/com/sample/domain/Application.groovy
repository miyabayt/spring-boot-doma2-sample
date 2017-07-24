package com.sample.domain

import com.sample.ComponentScanBasePackage
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication(scanBasePackageClasses = ComponentScanBasePackage)
class Application {
}
