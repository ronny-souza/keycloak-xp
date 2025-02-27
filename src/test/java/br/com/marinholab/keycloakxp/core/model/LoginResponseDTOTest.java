package br.com.marinholab.keycloakxp.core.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LoginResponseDTOTest {

    @Test
    @DisplayName("Should return a valid instance of login response DTO")
    void shouldReturnValidInstanceOfLoginResponseDTO() {
        String accessToken = "accessToken";
        Long expiresIn = 300L;
        Long refreshExpiresIn = 600L;
        String refreshToken = "refreshToken";
        String tokenType = "tokenType";
        String sessionState = "sessionState";
        String scope = "scope";

        LoginResponseDTO response = new LoginResponseDTO(
                accessToken,
                expiresIn,
                refreshExpiresIn,
                refreshToken,
                tokenType,
                sessionState,
                scope
        );

        assertEquals(accessToken, response.accessToken());
        assertEquals(expiresIn, response.expiresIn());
        assertEquals(refreshExpiresIn, response.refreshExpiresIn());
        assertEquals(refreshToken, response.refreshToken());
        assertEquals(tokenType, response.tokenType());
        assertEquals(sessionState, response.sessionState());
        assertEquals(scope, response.scope());
    }
}