package com.promovac.jolivoyage.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${api.base-url}")
    private String apiBaseUrl; // Injecte l'URL dynamique de l'API

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Gestion de la Trésorerie") // Titre de l'API
                        .version("1.0.0") // Version de l'API
                        .description("API pour la gestion de la trésorerie d'une agence de voyage. " +
                                "Permet de suivre les entrées et sorties d'argent, les réservations, les paiements, etc.")
                        .contact(new Contact()
                                .name("Support Agence de Voyage")
                                .email("support@agencevoyage.com")
                                .url("https://www.agencevoyage.com/contact"))
                        .license(new License()
                                .name("Licence MIT")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url(apiBaseUrl) // Utilise l'URL dynamique injectée
                                .description("Serveur d'API")
                ));
    }
}
