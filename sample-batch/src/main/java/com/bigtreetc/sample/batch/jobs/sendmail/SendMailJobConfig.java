package com.bigtreetc.sample.batch.jobs.sendmail;

import com.bigtreetc.sample.batch.listener.DefaultStepExecutionListener;
import com.bigtreetc.sample.domain.entity.SendMailQueue;
import lombok.val;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;

/** メール送信バッチ */
@Configuration
public class SendMailJobConfig {

  @Autowired JobBuilderFactory jobBuilderFactory;

  @Autowired StepBuilderFactory stepBuilderFactory;

  @Autowired TaskExecutor taskExecutor;

  @Bean
  public SendMailQueueItemReader itemReader() {
    val reader = new SendMailQueueItemReader();
    reader.setPageSize(100); // 100件ずつ取得するサンプル
    return reader;
  }

  @Bean
  public SendMailQueueProcessor processor() {
    return new SendMailQueueProcessor();
  }

  @Bean
  public SendMailQueueItemWriter itemWriter() {
    return new SendMailQueueItemWriter();
  }

  @Bean
  public JobExecutionListener sendMailJobListener() {
    return new SendMailJobListener();
  }

  @Bean
  public Step sendMailStep(
      SendMailQueueItemReader itemReader,
      SendMailQueueProcessor processor,
      SendMailQueueItemWriter itemWriter) {
    return stepBuilderFactory
        .get("sendMailStep")
        .listener(new DefaultStepExecutionListener())
        .<SendMailQueue, SendMailQueue>chunk(100)
        .reader(itemReader)
        .processor(processor)
        .writer(itemWriter)
        .taskExecutor(taskExecutor)
        .throttleLimit(1)
        .build();
  }

  @Bean
  public Job sendMailJob(@Qualifier("sendMailStep") Step step) {
    return jobBuilderFactory
        .get("sendMailJob")
        .incrementer(new RunIdIncrementer())
        .listener(sendMailJobListener())
        .flow(step)
        .end()
        .build();
  }
}
