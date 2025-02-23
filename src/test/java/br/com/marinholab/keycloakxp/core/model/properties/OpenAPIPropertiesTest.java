package br.com.marinholab.keycloakxp.core.model.properties;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class OpenAPIPropertiesTest {

    @Test
    @DisplayName("Should return a valid instance of OpenAPI properties class")
    void shouldReturnValidInstanceOfOpenAPIPropertiesClass() {
        String title = "title";
        String description = "description";
        String version = "1.0.0";
        String developer = "Ronyeri Marinho";
        String developerUrl = "developerUrl";
        String developerContact = "developerContact";

        OpenAPIProperties openAPIProperties = new OpenAPIProperties();
        openAPIProperties.setTitle(title);
        openAPIProperties.setDescription(description);
        openAPIProperties.setVersion(version);
        openAPIProperties.setDeveloper(developer);
        openAPIProperties.setDeveloperUrl(developerUrl);
        openAPIProperties.setDeveloperContact(developerContact);

        assertNotNull(openAPIProperties);
        assertEquals(title, openAPIProperties.getTitle());
        assertEquals(description, openAPIProperties.getDescription());
        assertEquals(version, openAPIProperties.getVersion());
        assertEquals(developer, openAPIProperties.getDeveloper());
        assertEquals(developerUrl, openAPIProperties.getDeveloperUrl());
        assertEquals(developerContact, openAPIProperties.getDeveloperContact());
    }
}