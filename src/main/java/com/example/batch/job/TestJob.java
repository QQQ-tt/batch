package com.example.batch.job;

import com.example.batch.entity.read.Read;
import com.example.batch.entity.write.Write;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.builder.MyBatisBatchItemWriterBuilder;
import org.mybatis.spring.batch.builder.MyBatisCursorItemReaderBuilder;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.HashMap;

/**
 * @author qtx
 * @since 2023/10/8 19:22
 */
@Component
public class TestJob {

    private final SqlSessionFactory readSqlSessionFactory;

    private final SqlSessionFactory writeSqlSessionFactory;

    private final JobRepository jobRepository;

    private final PlatformTransactionManager transactionManager;

    public TestJob(@Qualifier("readSqlSessionFactory") SqlSessionFactory readSqlSessionFactory,
                   @Qualifier("writeSqlSessionFactory") SqlSessionFactory writeSqlSessionFactory, JobRepository jobRepository,
                   PlatformTransactionManager transactionManager) {
        this.readSqlSessionFactory = readSqlSessionFactory;
        this.writeSqlSessionFactory = writeSqlSessionFactory;
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
    }

    @Bean
    public Job footballJob() {
        return new JobBuilder("footballJob", jobRepository).preventRestart()
                .start(step1())
                .build();
    }

    public Step step1() {
        return new StepBuilder("step1", jobRepository)
                .<Read, Write>chunk(1000, transactionManager)
                .reader(itemReader())
                .processor(item -> Write.builder()
                        .code(item.getCode())
                        .name(item.getName())
                        .build())
                .writer(itemWriter())
                .allowStartIfComplete(true)
                .build();
    }

    private ItemReader<? extends Read> itemReader() {
        return new MyBatisCursorItemReaderBuilder<Read>()
                .sqlSessionFactory(readSqlSessionFactory)
                .parameterValues(new HashMap<>())
                .queryId("")
                .build();
    }

    private ItemWriter<? super Write> itemWriter() {
        return new MyBatisBatchItemWriterBuilder<>()
                .sqlSessionFactory(writeSqlSessionFactory)
                .statementId("")
                .build();
    }

}
