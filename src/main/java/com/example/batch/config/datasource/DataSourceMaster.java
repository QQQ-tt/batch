package com.example.batch.config.datasource;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.example.batch.config.mybatis.MyMetaObjectHandler;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

/**
 * @author qtx
 * @since 2023/10/7 20:53
 */
@Configuration
public class DataSourceMaster {

    @Primary
    @Bean(name = "dataSource")
    @ConfigurationProperties(prefix = "spring.datasource.master")
    public HikariDataSource dataSource() {
        HikariDataSource build = DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .build();
        build.setPoolName("master");
        return build;
    }

    @Primary
    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSource") HikariDataSource dataSource, MybatisPlusInterceptor mybatisPlusInterceptor, MyMetaObjectHandler myMetaObjectHandler) throws Exception {
        MybatisConfiguration configuration = new MybatisConfiguration();
        configuration.setLogImpl(org.apache.ibatis.logging.stdout.StdOutImpl.class);
        configuration.setMapUnderscoreToCamelCase(Boolean.TRUE);
        configuration.setCallSettersOnNulls(Boolean.TRUE);
        MybatisSqlSessionFactoryBean sqlSessionFactoryBean = new MybatisSqlSessionFactoryBean();
        GlobalConfig globalConfig = new GlobalConfig();
        GlobalConfig.DbConfig config = new GlobalConfig.DbConfig();
        config.setIdType(IdType.AUTO);
        config.setLogicDeleteField("deleteFlag");
        config.setLogicDeleteValue("1");
        config.setLogicNotDeleteValue("0");
        globalConfig.setDbConfig(config);
        globalConfig.setMetaObjectHandler(myMetaObjectHandler);
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setConfiguration(configuration);
        sqlSessionFactoryBean.setGlobalConfig(globalConfig);
        sqlSessionFactoryBean.setPlugins(mybatisPlusInterceptor);
        // 这个是指定加载的xml 文件路径。
        sqlSessionFactoryBean.setMapperLocations(
                new PathMatchingResourcePatternResolver().getResources("classpath:mappers/master/*Mapper.xml"));
        return sqlSessionFactoryBean.getObject();
    }

    @Primary
    @Bean(name = "sqlSessionFactoryTemplate")
    public SqlSessionTemplate sqlSessionFactoryTemplate(@Qualifier("sqlSessionFactory") SqlSessionFactory sessionFactory) {
        return new SqlSessionTemplate(sessionFactory);
    }

    @Primary
    @Bean("transactionManager")
    public DataSourceTransactionManager transactionManager(@Qualifier("dataSource") HikariDataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

}
