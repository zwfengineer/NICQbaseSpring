package com.example.nicqbasespring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class NicqWebConfiguration {
    @Bean
    public WebMvcConfigurer corsConfigure(){
//  配置跨域请求，向localhost：1938开放跨域请求
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:1938")
                        .allowedMethods("PUT","GET","POST","OPTIONS")
                        .allowCredentials(true);
            }
        };
    }
}
