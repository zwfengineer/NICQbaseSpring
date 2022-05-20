package com.example.nicqbasespring.config;
import com.example.nicqbasespring.handler.AuthenticationExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


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
