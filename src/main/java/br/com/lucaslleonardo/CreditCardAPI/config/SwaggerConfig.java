package br.com.lucaslleonardo.CreditCardAPI.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {

        return new  OpenAPI().info(new Info()
                .title("CreditCard")
                .version("1.0")
                .description("API de sistema cartão de crédito")
        );


    }
}
