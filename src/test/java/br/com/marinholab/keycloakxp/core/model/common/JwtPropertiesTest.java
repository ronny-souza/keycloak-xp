package br.com.marinholab.keycloakxp.core.model.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JwtPropertiesTest {

    @Test
    @DisplayName("Should return preferred_username value when constant is called")
    void shouldReturnPreferredUsernameValueWhenConstantIsCalled() {
        assertEquals("preferred_username", JwtProperties.PREFERRED_USERNAME);
    }
}