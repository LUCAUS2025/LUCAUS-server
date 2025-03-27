package com.likelion13.lucaus_api.common.config;


import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.likelion13.lucaus_api.controller")
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Lucaus server API")
                        .version("1.0")
                        .description("API for Lucaus server")
                        .contact(new Contact()
                                .name("Choi eunsu")
                                .email("regulus08@cau.ac.kr")
                                ));
    }
}

