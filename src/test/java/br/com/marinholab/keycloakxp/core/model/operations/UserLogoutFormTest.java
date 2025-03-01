package br.com.marinholab.keycloakxp.core.model.operations;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserLogoutFormTest {

    @Test
    @DisplayName("Should return a valid instance of user logout form")
    void shouldReturnValidInstanceOfUserLogoutForm() {
        String refreshToken = "refreshToken";
        UserLogoutForm form = new UserLogoutForm(refreshToken);
        assertEquals(refreshToken, form.refreshToken());
    }
}