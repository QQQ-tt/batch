package com.example.batch.job;

import com.example.batch.entity.read.Read;
import com.example.batch.entity.write.Write;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * @author qtx
 * @since 2023/10/17
 */
@Slf4j
@Component
public class TestStep {

    private final PlatformTransactionManager transactionManager;

    private final JobRepository jobRepository;

    public TestStep(JobRepository jobRepository,
                    PlatformTransactionManager transactionManager) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
    }

    @Bean("testStepRead")
    public Step step(@Qualifier("itemWriterMybatis") ItemWriter<Write> writer,
                     @Qualifier("itemReaderMybatis") ItemReader<Read> reader) {
        return new StepBuilder("testStepRead", jobRepository)
                .<Read, Write>chunk(1000, transactionManager)
                .reader(reader)
                .processor(item -> Write.builder()
                        .code(item.getCode())
                        .name(item.getName())
                        .build())
                .writer(writer)
                .allowStartIfComplete(true)
                .build();
    }

}
