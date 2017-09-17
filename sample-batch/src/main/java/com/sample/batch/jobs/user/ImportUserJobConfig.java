package com.sample.batch.jobs.user;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
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

import com.sample.batch.listener.DefaultStepExecutionListener;
import com.sample.domain.dto.user.User;

import lombok.val;

/**
 * ユーザー情報取り込み
 */
@Configuration
@EnableBatchProcessing
public class ImportUserJobConfig {

    @Autowired
    JobBuilderFactory jobBuilderFactory;

    @Autowired
    StepBuilderFactory stepBuilderFactory;

    @Bean
    public ItemReader<User> userItemReader() {
        val reader = new FlatFileItemReader<User>();
        reader.setResource(new ClassPathResource("sample_users.csv"));
        reader.setLinesToSkip(1); // ヘッダーをスキップする
        reader.setLineMapper(new DefaultLineMapper<User>() {
            {
                setLineTokenizer(new DelimitedLineTokenizer() {
                    {
                        setNames(new String[] { "firstName", "lastName", "email", "tel", "zip", "address" });
                    }
                });
                setFieldSetMapper(new BeanWrapperFieldSetMapper<User>() {
                    {
                        setTargetType(User.class);
                    }
                });
            }
        });
        return reader;
    }

    @Bean
    public UserItemProcessor userItemProcessor() {
        return new UserItemProcessor();
    }

    @Bean
    public ItemWriter<User> userItemWriter() {
        return new UserItemWriter();
    }

    @Bean
    public JobExecutionListener importUserJobListener() {
        return new ImportUserJobListener();
    }

    @Bean
    public Job importUserJob() {
        return jobBuilderFactory.get("importUserJob").incrementer(new RunIdIncrementer())
                .listener(importUserJobListener()).flow(importUserStep()).end().build();
    }

    @Bean
    public Step importUserStep() {
        return stepBuilderFactory.get("importUserStep").listener(new DefaultStepExecutionListener())
                .<User, User>chunk(1000).reader(userItemReader()).processor(userItemProcessor())
                .writer(userItemWriter()).build();
    }
}
