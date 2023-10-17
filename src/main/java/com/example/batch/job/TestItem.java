package com.example.batch.job;

import com.example.batch.entity.read.Read;
import com.example.batch.entity.write.Write;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.MyBatisBatchItemWriter;
import org.mybatis.spring.batch.MyBatisCursorItemReader;
import org.mybatis.spring.batch.builder.MyBatisBatchItemWriterBuilder;
import org.mybatis.spring.batch.builder.MyBatisCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author qtx
 * @since 2023/10/17
 */
@Slf4j
@Configuration
public class TestItem {

    @Bean(name = "itemReaderMybatis")
    public MyBatisCursorItemReader<Read> itemReaderMybatis(@Qualifier("readSqlSessionFactory") SqlSessionFactory readSqlSessionFactory) {
        log.info("itemReader~~~~~~~~~~~~~");
        return new MyBatisCursorItemReaderBuilder<Read>()
                .sqlSessionFactory(readSqlSessionFactory)
//                .parameterValues(new HashMap<>())
                .queryId("com.example.batch.mapper.read.selectRead")
                .build();
    }


    @Bean(name = "itemWriterMybatis")
    public MyBatisBatchItemWriter<Write> itemWriterMybatis(@Qualifier("readSqlSessionFactory") SqlSessionFactory writeSqlSessionFactory) {
        log.info("itemWriter~~~~~~~~~~~~~");
        return new MyBatisBatchItemWriterBuilder<Write>()
                .sqlSessionFactory(writeSqlSessionFactory)
                .statementId("com.example.batch.mapper.write.inertWrite")
                .build();
    }
}
