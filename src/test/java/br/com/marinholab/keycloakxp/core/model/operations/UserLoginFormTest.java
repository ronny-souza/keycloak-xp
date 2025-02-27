package br.com.marinholab.keycloakxp.core.model.operations;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserLoginFormTest {

    @Test
    @DisplayName("Should return a valid instance of user login form")
    void shouldReturnValidInstanceOfUserLoginForm() {
        String username = "user";
        String password = "Str0ngP@ssword";

        UserLoginForm form = new UserLoginForm(username, password);
        assertEquals(username, form.username());
        assertEquals(password, form.password());
    }
}