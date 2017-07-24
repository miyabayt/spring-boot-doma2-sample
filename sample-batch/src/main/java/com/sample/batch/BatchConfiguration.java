package com.sample.batch;

import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.sample.batch.listener.DefaultJobExecutionListener;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Bean
    public JobExecutionListener listener() {
        return new DefaultJobExecutionListener();
    }
}
