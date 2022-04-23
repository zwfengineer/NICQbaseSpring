package com.example.nicqbasespring.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
// open api doc 配置
public class NicqOpenApiConfig {
    private License license(){
        return new License();
    }
    private Info info(){
        return new Info()
                .title("nicq api document")
                .description("???")
                .version("0.0.1")
                .license(license());
    }
    private ExternalDocumentation externalDocumentation(){
        return new ExternalDocumentation()
                .description("2333")
                .url("null");
    }
    @Bean
    public OpenAPI springShopOpenApi(){
        return new OpenAPI()
                .info(info())
                .externalDocs(externalDocumentation());
    }

}
