package com.bigtreetc.sample.batch.jobs.birthdayMail;

import com.bigtreetc.sample.batch.listener.DefaultStepExecutionListener;
import com.bigtreetc.sample.domain.entity.SendMailQueue;
import com.bigtreetc.sample.domain.entity.User;
import lombok.val;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;

/** バースデーメール作成バッチ */
@Configuration
public class BirthdayMailJobConfig {

  @Autowired JobBuilderFactory jobBuilderFactory;

  @Autowired StepBuilderFactory stepBuilderFactory;

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
  public Step birthdayMailStep() {
    return stepBuilderFactory
        .get("birthdayMailStep")
        .listener(new DefaultStepExecutionListener())
        .<User, SendMailQueue>chunk(100)
        .reader(birthdayMailUserItemReader())
        .processor(birthdayMailProcessor())
        .writer(birthdayMailItemWriter())
        .taskExecutor(taskExecutor)
        // 2つのスレッドで処理するサンプル
        .throttleLimit(2)
        .build();
  }

  @Bean
  public Job birthdayMailJob() {
    return jobBuilderFactory
        .get("birthdayMailJob")
        .incrementer(new RunIdIncrementer())
        .listener(birthdayMailJobListener())
        .flow(birthdayMailStep())
        .end()
        .build();
  }
}
