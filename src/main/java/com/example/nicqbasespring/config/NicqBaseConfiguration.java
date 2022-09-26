package com.example.nicqbasespring.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import javax.sql.DataSource;


@Configuration
@ComponentScan(
        basePackages = {
                "com.example.nicqbasespring.service",
                "com.example.nicqbasespring.dao",
                "com.example.nicqbasespring.controller",
                "com.example.nicqbasespring.entries",
                "com.example.nicqbasespring.handler"
        }
)
@Slf4j
@EnableAspectJAutoProxy (proxyTargetClass = true)
@EnableTransactionManagement
public class NicqBaseConfiguration {
        @Bean(name="dataSource")
        public DruidDataSource getDataSource(){
                DruidDataSource dataSource = new DruidDataSource();
                dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
                dataSource.setUrl("jdbc:mysql://localhost:3306/nicqdatabase");
                dataSource.setUsername("root");
                dataSource.setPassword("root");
                return dataSource;
        }

        @Bean(name="nicqjdbcTemplate")
        @Autowired(required = false)
        public JdbcTemplate getJdbctemplate(DataSource dataSource){
                JdbcTemplate jdbcTemplate = new JdbcTemplate();
                jdbcTemplate.setDataSource(dataSource);
                return jdbcTemplate;
        }
        @Bean(name="DataSourceTransactionManager")
        @Autowired(required = false)
        public DataSourceTransactionManager getDatasourceTransactionManager(DataSource datasource){
                DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
                dataSourceTransactionManager.setDataSource(datasource);
                return dataSourceTransactionManager;
        }
        @Bean
        public ObjectMapper getObjectMapper(){
                return new ObjectMapper();
        }

        @Bean
        public PasswordEncoder passwordEncoder(){
                return new BCryptPasswordEncoder(10);
        }

}
