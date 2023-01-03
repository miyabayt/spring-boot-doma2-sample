package com.bigtreetc.sample.batch.jobs.staff;

import com.bigtreetc.sample.batch.listener.DefaultStepExecutionListener;
import com.bigtreetc.sample.domain.entity.Staff;
import lombok.val;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

/** 担当者情報取込 */
@Configuration
public class ImportStaffJobConfig {

  @Autowired JobBuilderFactory jobBuilderFactory;

  @Autowired StepBuilderFactory stepBuilderFactory;

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
  public Step importStaffStep() {
    return stepBuilderFactory
        .get("importStaffStep")
        .listener(new DefaultStepExecutionListener())
        .<Staff, Staff>chunk(100)
        .reader(staffItemReader())
        .processor(staffItemProcessor())
        .writer(staffItemWriter())
        .build();
  }

  @Bean
  public Job importStaffJob() {
    return jobBuilderFactory
        .get("importStaffJob")
        .incrementer(new RunIdIncrementer())
        .listener(importStaffJobListener())
        .flow(importStaffStep())
        .end()
        .build();
  }
}
