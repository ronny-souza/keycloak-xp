package br.com.marinholab.keycloakxp.core.model.properties;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class KeycloakClientPropertiesTest {

    @Test
    @DisplayName("Should return a valid instance of KeycloakClientProperties")
    void shouldReturnValidInstanceOfKeycloakClientProperties() {
        KeycloakClientProperties keycloakClientProperties = new KeycloakClientProperties();
        keycloakClientProperties.setClientId("clientId");
        keycloakClientProperties.setClientSecret("clientSecret");

        assertNotNull(keycloakClientProperties);
        assertEquals("clientId", keycloakClientProperties.getClientId());
        assertEquals("clientSecret", keycloakClientProperties.getClientSecret());
    }
}