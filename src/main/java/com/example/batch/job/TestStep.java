package com.example.batch.job;

import com.example.batch.entity.read.Read;
import com.example.batch.entity.write.ListWrite;
import com.example.batch.entity.write.Write;
import com.example.batch.mapper.write.WriteMapper;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.batch.MyBatisBatchItemWriter;
import org.mybatis.spring.batch.MyBatisCursorItemReader;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.BeanUtils;
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

    private final WriteMapper writeMapper;

    private static final ListWrite cachedDataList = new ListWrite();

    private static final int SAVE_SIZE = 500;

    public TestStep(JobRepository jobRepository,
                    PlatformTransactionManager transactionManager, WriteMapper writeMapper) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
        this.writeMapper = writeMapper;
    }


    @Bean("initStep")
    public Step initStep() {
        return new StepBuilder("initStep", jobRepository).tasklet((contribution, chunkContext) -> {
                            if (!cachedDataList.getItems()
                                    .isEmpty()) {
                                cachedDataList.getItems()
                                        .clear();
                            }
                            return RepeatStatus.FINISHED;
                        },
                        transactionManager)
                .allowStartIfComplete(true)
                .build();
    }


    @Bean("testStepRead")
    public Step step(@Qualifier("itemReaderMybatis") MyBatisCursorItemReader<Read> itemReaderMybatis,
                     @Qualifier("itemWriterMybatis") MyBatisBatchItemWriter<ListWrite> itemWriterMybatis) {
        return new StepBuilder("testStepRead", jobRepository)
                .<Read, ListWrite>chunk(SAVE_SIZE, transactionManager)
                .reader(itemReaderMybatis)
                .processor(item -> {
                    if (cachedDataList.getItems()
                            .size() >= SAVE_SIZE) {
                        cachedDataList.getItems()
                                .clear();
                    }
                    Write write = new Write();
                    BeanUtils.copyProperties(item, write);
                    cachedDataList.getItems()
                            .add(write);
                    if (cachedDataList.getItems()
                            .size() >= SAVE_SIZE) {
                        return cachedDataList;
                    }
                    return null;
                })
                .writer(itemWriterMybatis)
                .allowStartIfComplete(true)
                .build();
    }

    @Bean("taskStep")
    public Step taskStep() {
        return new StepBuilder("taskStep", jobRepository).tasklet((contribution, chunkContext) -> {
                            if (!cachedDataList.getItems()
                                    .isEmpty() && cachedDataList.getItems()
                                    .size() != SAVE_SIZE) {
                                writeMapper.inertBatchWrite(cachedDataList);
                                log.info("存储数据库成功！再次存储：{}", cachedDataList.getItems()
                                        .size());
                            }
                            return RepeatStatus.FINISHED;
                        },
                        transactionManager)
                .allowStartIfComplete(true)
                .build();
    }

}
