package br.com.marinholab.keycloakxp.configuration;

import br.com.marinholab.keycloakxp.model.properties.OpenAPIProperties;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfiguration {

    @Bean
    public OpenAPI customOpenAPI(OpenAPIProperties properties) {
        Contact contact = new Contact()
                .name(properties.getDeveloper())
                .email(properties.getDeveloperContact())
                .url(properties.getDeveloperUrl());

        Info info = new Info()
                .title(properties.getTitle())
                .description(properties.getDescription())
                .version(properties.getVersion())
                .contact(contact);

        return new OpenAPI().components(new Components()).info(info);
    }
}
