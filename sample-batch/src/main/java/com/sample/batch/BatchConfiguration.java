package com.sample.batch;

import javax.sql.DataSource;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.converter.JobParametersConverter;
import org.springframework.batch.core.jsr.JsrJobParametersConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.sample.batch.jobs.SingleJobCommandLineRunner;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Bean
    public JobParametersConverter jobParametersConverter(DataSource dataSource) {
        return new JsrJobParametersConverter(dataSource);
    }

    @Bean
    public SingleJobCommandLineRunner commandLineRunner() {
        return new SingleJobCommandLineRunner();
    }
}
