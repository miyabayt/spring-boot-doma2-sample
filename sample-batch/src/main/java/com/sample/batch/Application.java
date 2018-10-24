package com.sample.batch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.sample.ComponentScanBasePackage;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

@SpringBootApplication(scanBasePackageClasses = ComponentScanBasePackage.class)
@Slf4j
public class Application {

    public static void main(String[] args) throws Exception {

        try {
            val application = new SpringApplication(Application.class);
            application.setWebApplicationType(WebApplicationType.NONE);

            val context = application.run(args);
            val exitCode = SpringApplication.exit(context);
            System.exit(exitCode);
        } catch (Throwable t) {
            log.error("failed to run. ", t);
            System.exit(1);
        }
    }
}
