package com.example.nicqbasespring.config;

import ch.qos.logback.core.net.SocketConnector;
import com.example.nicqbasespring.handler.AuthenticationExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.SpringSecurityMessageSource;

@Configuration
public class NicqSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    AuthenticationExceptionHandler authenticationExceptionHandler;
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/**");
        HttpSecurity httpSecurity = this.getHttp();
        httpSecurity.exceptionHandling()
                .authenticationEntryPoint(authenticationExceptionHandler);
        httpSecurity.csrf().disable();
    }
}
