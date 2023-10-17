package com.example.batch.job;

import com.example.batch.entity.read.Read;
import com.example.batch.entity.write.Write;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.batch.MyBatisBatchItemWriter;
import org.mybatis.spring.batch.MyBatisCursorItemReader;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * @author qtx
 * @since 2023/10/17
 */
@Slf4j
@Configuration
public class TestStep {

    private final PlatformTransactionManager transactionManager;

    private final JobRepository jobRepository;

    public TestStep(JobRepository jobRepository,
                    PlatformTransactionManager transactionManager) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
    }

    @Bean("testStepRead")
    public Step step(@Qualifier("itemReaderMybatis") MyBatisCursorItemReader<Read> itemReaderMybatis,
                     @Qualifier("itemWriterMybatis") MyBatisBatchItemWriter<Write> itemWriterMybatis) {
        return new StepBuilder("testStepRead", jobRepository)
                .<Read, Write>chunk(500, transactionManager)
                .reader(itemReaderMybatis)
                .processor(item -> Write.builder()
                        .code(item.getCode())
                        .name(item.getName())
                        .build())
                .writer(itemWriterMybatis)
                .allowStartIfComplete(true)
                .build();
    }

}
