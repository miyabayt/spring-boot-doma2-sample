package com.bigtreetc.sample.batch.jobs.birthdayMail;

import com.bigtreetc.sample.batch.listener.DefaultStepExecutionListener;
import com.bigtreetc.sample.domain.entity.SendMailQueue;
import com.bigtreetc.sample.domain.entity.User;
import lombok.val;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

/** バースデーメール作成バッチ */
@Configuration
public class BirthdayMailJobConfig {

  @Autowired TaskExecutor taskExecutor;

  @Bean
  public ItemReader<User> birthdayMailUserItemReader() {
    val reader = new BirthdayMailUserItemReader();
    reader.setPageSize(100); // 100件ずつ取得するサンプル
    return reader;
  }

  @Bean
  public ItemProcessor<User, SendMailQueue> birthdayMailProcessor() {
    // UserをSendMailQueueに変換する
    return new BirthdayMailProcessor();
  }

  @Bean
  public ItemWriter<SendMailQueue> birthdayMailItemWriter() {
    return new BirthdayMailItemWriter();
  }

  @Bean
  public JobExecutionListener birthdayMailJobListener() {
    return new BirthdayMailJobListener();
  }

  @Bean
  public Step birthdayMailStep(
      JobRepository jobRepository, PlatformTransactionManager transactionManager) {
    return new StepBuilder("birthdayMailStep", jobRepository)
        .listener(new DefaultStepExecutionListener())
        .<User, SendMailQueue>chunk(100, transactionManager)
        .reader(birthdayMailUserItemReader())
        .processor(birthdayMailProcessor())
        .writer(birthdayMailItemWriter())
        .taskExecutor(taskExecutor)
        .build();
  }

  @Bean
  public Job birthdayMailJob(
      JobRepository jobRepository, @Qualifier("birthdayMailStep") Step step) {
    return new JobBuilder("birthdayMailJob", jobRepository)
        .incrementer(new RunIdIncrementer())
        .listener(birthdayMailJobListener())
        .flow(step)
        .end()
        .build();
  }
}
