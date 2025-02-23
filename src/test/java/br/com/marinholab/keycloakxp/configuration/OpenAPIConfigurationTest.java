package br.com.marinholab.keycloakxp.configuration;

import br.com.marinholab.keycloakxp.core.model.properties.OpenAPIProperties;
import io.swagger.v3.oas.models.OpenAPI;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class OpenAPIConfigurationTest {

    @Test
    @DisplayName("Should return a valid instance of OpenAPI")
    void shouldReturnValidInstanceOfOpenAPI() {
        OpenAPIProperties openApiPropertiesAsMock = mock(OpenAPIProperties.class);
        when(openApiPropertiesAsMock.getDeveloper()).thenReturn("Ronyeri Marinho");
        when(openApiPropertiesAsMock.getDeveloperContact()).thenReturn("keycloakxp@example.com");
        when(openApiPropertiesAsMock.getDeveloperUrl()).thenReturn("keycloakxp.com");
        when(openApiPropertiesAsMock.getTitle()).thenReturn("Keycloak XP");
        when(openApiPropertiesAsMock.getDescription()).thenReturn("Some cool description here");
        when(openApiPropertiesAsMock.getVersion()).thenReturn("1.0.0-dev");

        OpenAPIConfiguration openAPIConfiguration = new OpenAPIConfiguration();

        OpenAPI openAPI = openAPIConfiguration.customOpenAPI(openApiPropertiesAsMock);
        assertNotNull(openAPI);
        assertNotNull(openAPI.getInfo());
        assertNotNull(openAPI.getInfo().getContact());
        assertEquals(openApiPropertiesAsMock.getDeveloper(), openAPI.getInfo().getContact().getName());
        assertEquals(openApiPropertiesAsMock.getDeveloperContact(), openAPI.getInfo().getContact().getEmail());
        assertEquals(openApiPropertiesAsMock.getDeveloperUrl(), openAPI.getInfo().getContact().getUrl());
        assertEquals(openApiPropertiesAsMock.getTitle(), openAPI.getInfo().getTitle());
        assertEquals(openApiPropertiesAsMock.getDescription(), openAPI.getInfo().getDescription());
        assertEquals(openApiPropertiesAsMock.getVersion(), openAPI.getInfo().getVersion());
    }
}