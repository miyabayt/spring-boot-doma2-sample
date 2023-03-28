package com.bigtreetc.sample.batch.jobs.staff;

import com.bigtreetc.sample.batch.listener.DefaultStepExecutionListener;
import com.bigtreetc.sample.domain.entity.Staff;
import lombok.val;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

/** 担当者情報取込 */
@Configuration
public class ImportStaffJobConfig {

  @Bean
  public ItemReader<Staff> staffItemReader() {
    val reader = new FlatFileItemReader<Staff>();
    reader.setResource(new ClassPathResource("sample_staffs.csv"));
    reader.setLinesToSkip(1); // ヘッダーをスキップする
    reader.setLineMapper(
        new DefaultLineMapper<>() {
          {
            setLineTokenizer(
                new DelimitedLineTokenizer() {
                  {
                    setNames("firstName", "lastName", "email", "tel");
                  }
                });
            setFieldSetMapper(
                new BeanWrapperFieldSetMapper<>() {
                  {
                    setTargetType(Staff.class);
                  }
                });
          }
        });
    return reader;
  }

  @Bean
  public StaffItemProcessor staffItemProcessor() {
    return new StaffItemProcessor();
  }

  @Bean
  public ItemWriter<Staff> staffItemWriter() {
    return new StaffItemWriter();
  }

  @Bean
  public JobExecutionListener importStaffJobListener() {
    return new ImportStaffJobListener();
  }

  @Bean
  public Step importStaffStep(
      JobRepository jobRepository, PlatformTransactionManager transactionManager) {
    return new StepBuilder("importStaffStep", jobRepository)
        .listener(new DefaultStepExecutionListener())
        .<Staff, Staff>chunk(100, transactionManager)
        .reader(staffItemReader())
        .processor(staffItemProcessor())
        .writer(staffItemWriter())
        .build();
  }

  @Bean
  public Job importStaffJob(JobRepository jobRepository, @Qualifier("importStaffStep") Step step) {
    return new JobBuilder("importStaffJob", jobRepository)
        .incrementer(new RunIdIncrementer())
        .listener(importStaffJobListener())
        .flow(step)
        .end()
        .build();
  }
}
