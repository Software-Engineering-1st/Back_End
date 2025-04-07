package com.se.dandan.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(customInfo());
    }

    private Info customInfo() {
        return new Info()
                .title("DANDAN API Document")
                .version("1.0")
                .description("DANDAN API 문서");
    }
}
