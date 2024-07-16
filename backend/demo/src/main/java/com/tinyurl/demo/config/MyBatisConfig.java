package com.tinyurl.demo.config;

import javax.sql.DataSource;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
// Add @org.mybatis.spring.annotation.MapperScan to enable scanning of Mapper interfaces.
@MapperScan("com.tinyurl.demo.mapper")
public class MyBatisConfig {
    @Bean
    public DataSource dataSource() {
        return DataSourceBuilder.create().url("jdbc:sqlite:db/database.tinyurl").build();
    }

    // Define the org.mybatis.spring.SqlSessionFactoryBean as a Bean.
    // This enables the generation of SqlSessionFactory using SqlSessionFactoryBean.
    @Bean
    public SqlSessionFactoryBean sqlSessionFactory() {
        SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
        // Configure the data source. When issuing SQL within MyBatis operations, 
        // connections will be obtained from the data source specified here.
        sessionFactoryBean.setDataSource(dataSource());
        // Specify the MyBatis configuration file.
        // place the configuration file directly under resources.
        sessionFactoryBean.setConfigLocation(new ClassPathResource("/myBatis-config.xml"));
        return sessionFactoryBean;
    }

    // Define the transaction manager bean.
    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }
}
