package it.trinex.spespappbe.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configurazione OpenAPI/Swagger per documentare le API e il meccanismo di sicurezza Bearer JWT.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI uniconnectOpenAPI() {
        final String securitySchemeName = "bearerAuth";
        return new OpenAPI()
                .info(
                        new Info()
                                .title("Spespapp API")
                                .version("v1")
                                .description("Documentazione delle API di Spespapp"))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .servers(List.of(
                        new Server().url("http://localhost:8080/api"),
                        new Server().url("https://spespapp.ndrw.cat/api")
                ))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT"))
                );
    }
}
