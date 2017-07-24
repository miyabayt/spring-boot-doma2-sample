package com.sample.batch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.sample.ComponentScanBasePackage;

import lombok.val;

@SpringBootApplication(scanBasePackageClasses = ComponentScanBasePackage.class)
public class Application {

    public static void main(String[] args) throws Exception {
        val application = new SpringApplication(Application.class);
        application.setWebEnvironment(false);

        val context = application.run(args);
        val exitCode = SpringApplication.exit(context);
        System.exit(exitCode);
    }
}
