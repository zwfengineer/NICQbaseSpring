package com.example.nicqbasespring.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import javax.sql.DataSource;
import java.util.logging.Logger;


@Configuration
@ComponentScan(
        basePackages = {
                "com.example.nicqbasespring.service",
                "com.example.nicqbasespring.dao",
                "com.example.nicqbasespring.controller",
                "com.example.nicqbasespring.entries"
        }
)
@EnableTransactionManagement
public class NicqBaseConfiguration {
        @Bean(name="Logger")
        public Logger getLogger(){
                return  Logger.getLogger(getClass().getName());
        }
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
        @Bean(name="nicqDataSourceTransactionManager")
        @Autowired(required = false)
        public DataSourceTransactionManager getDatasourceTransactionManager(DataSource datasource){
                DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
                dataSourceTransactionManager.setDataSource(datasource);
                return dataSourceTransactionManager;
        }
}
