package com.sample.web.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.sample.ComponentScanBasePackage;

@SpringBootApplication(scanBasePackageClasses = { ComponentScanBasePackage.class })
@EnableScheduling
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
