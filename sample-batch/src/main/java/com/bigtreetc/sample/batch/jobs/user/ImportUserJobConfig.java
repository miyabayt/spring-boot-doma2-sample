package com.bigtreetc.sample.batch.jobs.user;

import com.bigtreetc.sample.batch.listener.DefaultStepExecutionListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

/** 顧客マスタ取込 */
@Configuration
public class ImportUserJobConfig {

  @Bean
  public JobExecutionListener importUserJobListener() {
    return new ImportUserJobListener();
  }

  @Bean
  public Step importUserStep(
      JobRepository jobRepository, PlatformTransactionManager transactionManager) {
    return new StepBuilder("importUserStep", jobRepository)
        .listener(new DefaultStepExecutionListener())
        .tasklet(importUserTasklet(), transactionManager)
        .build();
  }

  @Bean
  public Job importUserJob(JobRepository jobRepository, @Qualifier("importUserStep") Step step) {
    return new JobBuilder("importUserJob", jobRepository)
        .incrementer(new RunIdIncrementer())
        .listener(importUserJobListener())
        .flow(step)
        .end()
        .build();
  }

  @Bean
  public Tasklet importUserTasklet() {
    return new ImportUserTasklet();
  }
}
