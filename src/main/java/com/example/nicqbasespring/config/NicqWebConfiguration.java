package com.example.nicqbasespring.config;

import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class NicqWebConfiguration {
    @Bean
    public WebMvcConfigurer corsConfigure(){
//  配置跨域请求，向localhost：1938开放跨域请求
        return new WebMvcConfigurer() {
            public void addCorsMappings(@NotNull CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOriginPatterns("*")
//                        .allowedOrigins("http://192.168.10.1:1938")
                        .allowedMethods("*")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
    @Bean
//    配置 User类中几个默认属性 {userpermission,usergender,useravatar}
    public MappingJackson2HttpMessageConverter  getMappingJackson2HttpMessageConverter(){
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter=new MappingJackson2HttpMessageConverter();
        ObjectMapper objectMapper = new ObjectMapper();
        InjectableValues.Std injectableValues = new InjectableValues.Std();
        injectableValues.addValue("UserPermission","normal");
        injectableValues.addValue("UserGender","保密");
        injectableValues.addValue("UserAvatar","0");
        objectMapper.setInjectableValues(injectableValues);
        mappingJackson2HttpMessageConverter.setObjectMapper(objectMapper);
        List<MediaType> list = new ArrayList<>();
        list.add(MediaType.APPLICATION_JSON);
        mappingJackson2HttpMessageConverter.setSupportedMediaTypes(list);
        return mappingJackson2HttpMessageConverter;
    }

    @Bean
    public ServerEndpointExporter  serverEndpointExporter(){
        return new ServerEndpointExporter();
    }

}



