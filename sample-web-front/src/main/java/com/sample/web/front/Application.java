package com.sample.web.front;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.sample.ComponentScanBasePackage;

@SpringBootApplication(scanBasePackageClasses = { ComponentScanBasePackage.class })
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
