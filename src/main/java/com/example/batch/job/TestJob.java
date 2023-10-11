package com.example.batch.job;

import com.example.batch.entity.read.Read;
import com.example.batch.entity.write.Write;
import org.mybatis.spring.batch.MyBatisBatchItemWriter;
import org.mybatis.spring.batch.builder.MyBatisBatchItemWriterBuilder;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * @author qtx
 * @since 2023/10/8 19:22
 */
@Component
public class TestJob {

    private final JobRepository jobRepository;

    private final PlatformTransactionManager transactionManager;

    public TestJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
    }

    @Bean
    public Job footballJob() {
        return new JobBuilder("footballJob", jobRepository).preventRestart().start(step1())
                .build();
    }

    public Step step1() {
        return new StepBuilder("step1", jobRepository)
                .<Read, Write>chunk(1000, transactionManager)
                .reader(itemReader())

                .writer(itemWriter())
                .build();
    }

    private ItemWriter<? super Write> itemWriter() {
//        MyBatisBatchItemWriter
        return null;
    }

    private ItemReader<? extends Read> itemReader() {
        return Read::new;
    }
}
