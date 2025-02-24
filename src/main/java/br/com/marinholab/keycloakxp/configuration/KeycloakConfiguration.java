package br.com.marinholab.keycloakxp.configuration;

import br.com.marinholab.keycloakxp.core.model.properties.KeycloakProperties;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakConfiguration {

    @Bean
    Keycloak newKeycloakInstance(KeycloakProperties keycloakProperties) {
        return KeycloakBuilder.builder()
                .serverUrl(keycloakProperties.getServer())
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .realm(keycloakProperties.getRealm())
                .clientId(keycloakProperties.getDefaultClient().getClientId())
                .clientSecret(keycloakProperties.getDefaultClient().getClientSecret())
                .build();
    }
}
