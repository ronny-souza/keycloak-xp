package br.com.marinholab.keycloakxp.core.model.properties;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

class KeycloakPropertiesTest {

    @Test
    @DisplayName("Should return a valid instance of KeycloakProperties")
    void shouldReturnValidInstanceOfKeycloakProperties() {
        KeycloakClientProperties keycloakClientPropertiesAsMock = mock(KeycloakClientProperties.class);
        String server = "server";
        String realm = "realm";
        Map<String, KeycloakClientProperties> clients = Map.of("default", keycloakClientPropertiesAsMock);

        KeycloakProperties keycloakProperties = new KeycloakProperties();
        keycloakProperties.setServer(server);
        keycloakProperties.setRealm(realm);
        keycloakProperties.setClients(clients);

        assertNotNull(keycloakProperties);
        assertEquals(server, keycloakProperties.getServer());
        assertEquals(realm, keycloakProperties.getRealm());
        assertEquals(clients, keycloakProperties.getClients());
        assertEquals(keycloakClientPropertiesAsMock, keycloakProperties.getDefaultClient());
    }
}