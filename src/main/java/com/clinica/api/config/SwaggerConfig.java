package com.clinica.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Clínica Médica API")
                        .description("""
                                API REST para gerenciamento de clínica médica.
                                
                                Funcionalidades:
                                - Cadastro e gestão de médicos
                                - Cadastro e gestão de pacientes
                                - Agendamento e cancelamento de consultas
                                - Listagem com filtros e paginação
                                """)
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("Felipe Cortez")
                                .email("felipe06cortez@gmail.com")
                                .url("https://github.com/OnurynFFC"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")));
    }
}
