package com.example.batch.config;

import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Data;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

/**
 * @author qtx
 * @since 2023/10/7 20:53
 */
@Data
@Configurable
@ConfigurationProperties(prefix = "spring.datasource-write")
@MapperScan(basePackages = "com.example.mapper.write", sqlSessionFactoryRef = "readSqlSessionFactory")
public class DataSourceWrite {

    private String username;

    private String password;

    private String driverClassName;

    private String url;

    private static final String dataSourceName = "writeDataSource";

    @Bean(dataSourceName)
    public HikariDataSource writeDataSource(){
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setDriverClassName(driverClassName);
        dataSource.setJdbcUrl(url);
        return dataSource;
    }

    @Bean("writeSqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier(dataSourceName) HikariDataSource dataSource) throws Exception {
        MybatisSqlSessionFactoryBean sqlSessionFactoryBean = new MybatisSqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        // 这个是指定加载的xml 文件路径。
        sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mappers/write/*Mapper.xml"));
        return sqlSessionFactoryBean.getObject();
    }


    @Bean("writeTransactionManager")
    public DataSourceTransactionManager transactionManager(@Qualifier(dataSourceName) HikariDataSource dataSource){
        return new DataSourceTransactionManager(dataSource);
    }

}