package com.example.batch.job;

import com.example.batch.entity.read.Read;
import com.example.batch.entity.write.ListWrite;
import com.example.batch.entity.write.Write;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.batch.MyBatisBatchItemWriter;
import org.mybatis.spring.batch.MyBatisCursorItemReader;
import org.mybatis.spring.batch.builder.MyBatisBatchItemWriterBuilder;
import org.mybatis.spring.batch.builder.MyBatisCursorItemReaderBuilder;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author qtx
 * @since 2023/10/17
 */
@Slf4j
@Configuration
public class TestItem {

    @StepScope
    @Bean(name = "itemReaderMybatis")
    public MyBatisCursorItemReader<Read> itemReaderMybatis(@Qualifier("readSqlSessionFactory") SqlSessionFactory readSqlSessionFactory, @Value("#{@datesParameters}") Map<String, Object> datesParameters) {
        log.info("itemReader~~~~~~~~~~~~~");
        return new MyBatisCursorItemReaderBuilder<Read>()
                .sqlSessionFactory(readSqlSessionFactory)
                .parameterValues(datesParameters)
                .queryId("com.example.batch.mapper.read.ReadMapper.selectRead")
                .build();
    }


    @StepScope
    @Bean(name = "itemWriterMybatis")
    public MyBatisBatchItemWriter<ListWrite> itemWriterMybatis(@Qualifier("writeSqlSessionFactory") SqlSessionFactory writeSqlSessionFactory, @Qualifier("writeSqlSessionFactoryTemplate") SqlSessionTemplate sqlSessionTemplate) {
        log.info("itemWriter~~~~~~~~~~~~~");
        return new MyBatisBatchItemWriterBuilder<ListWrite>()
                .sqlSessionFactory(writeSqlSessionFactory)
                .sqlSessionTemplate(sqlSessionTemplate)
//                .statementId("com.example.batch.mapper.write.WriteMapper.inertWrite")
                .statementId("com.example.batch.mapper.write.WriteMapper.inertBatchWrite")
                .assertUpdates(Boolean.TRUE)
                .build();
    }

    @StepScope
    @Bean
    public Map<String, Object> datesParameters(
            @Value("#{jobParameters['time']}") LocalDateTime time,
            @Value("#{jobParameters['name']}") String name) {
        Map<String, Object> map = new HashMap<>();
        map.put("time", time);
        map.put("name", name);
        return map;
    }
}
