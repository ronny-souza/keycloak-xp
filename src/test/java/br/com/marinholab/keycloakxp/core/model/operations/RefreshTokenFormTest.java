package br.com.marinholab.keycloakxp.core.model.operations;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RefreshTokenFormTest {

    @Test
    @DisplayName("Should return a valid instance of refresh token form")
    void shouldReturnValidInstanceOfRefreshTokenForm() {
        String refreshToken = "refreshToken";
        RefreshTokenForm form = new RefreshTokenForm(refreshToken);
        assertEquals(refreshToken, form.refreshToken());
    }
}