package com.magnetron.billing.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customeOpenAPI(){
        return new OpenAPI()
            .info(
                new Info()
                    .title("API REST DE FACTURACION")
                    .version("v1.0")
                    .description("Rest de facturación de productos")
                    .termsOfService("")
                    .license(
                        new License()
                            .name("Open source")
                            .url("")
                    )
            );
    }
}
