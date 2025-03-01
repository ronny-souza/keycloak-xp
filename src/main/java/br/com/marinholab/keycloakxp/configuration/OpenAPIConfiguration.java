package br.com.marinholab.keycloakxp.configuration;

import br.com.marinholab.keycloakxp.core.model.properties.OpenAPIProperties;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfiguration {

    @Bean
    OpenAPI customOpenAPI(OpenAPIProperties properties) {
        Contact contact = new Contact()
                .name(properties.getDeveloper())
                .email(properties.getDeveloperContact())
                .url(properties.getDeveloperUrl());

        Info info = new Info()
                .title(properties.getTitle())
                .description(properties.getDescription())
                .version(properties.getVersion())
                .contact(contact);

        Components components = new Components()
                .addSecuritySchemes(
                        "Bearer-Token",
                        new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .in(SecurityScheme.In.HEADER)
                                .name("Authorization")
                );

        return new OpenAPI()
                .components(components)
                .addSecurityItem(new SecurityRequirement().addList("Bearer-Token"))
                .info(info);
    }

    @Bean
    GroupedOpenApi customGroupedOpenApi() {
        return GroupedOpenApi
                .builder()
                .group("custom-grouped-openapi-configuration")
                .displayName("Custom Grouped OpenAPI Configuration")
                .packagesToScan("br.com.marinholab.keycloakxp.core")
                .build();
    }
}
