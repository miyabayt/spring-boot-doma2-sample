package com.bigtreetc.sample.batch.jobs.user;

import com.bigtreetc.sample.batch.listener.DefaultStepExecutionListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** ユーザー情報取込 */
@Configuration
public class ImportUserJobConfig {

  @Autowired JobBuilderFactory jobBuilderFactory;

  @Autowired StepBuilderFactory stepBuilderFactory;

  @Bean
  public JobExecutionListener importUserJobListener() {
    return new ImportUserJobListener();
  }

  @Bean
  public Step importUserStep() {
    return stepBuilderFactory
        .get("importUserStep")
        .listener(new DefaultStepExecutionListener())
        .tasklet(importUserTasklet())
        .build();
  }

  @Bean
  public Job importUserJob() {
    return jobBuilderFactory
        .get("importUserJob")
        .incrementer(new RunIdIncrementer())
        .listener(importUserJobListener())
        .flow(importUserStep())
        .end()
        .build();
  }

  @Bean
  public Tasklet importUserTasklet() {
    return new ImportUserTasklet();
  }
}
