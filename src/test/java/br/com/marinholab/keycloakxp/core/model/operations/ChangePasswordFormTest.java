package br.com.marinholab.keycloakxp.core.model.operations;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ChangePasswordFormTest {

    @Test
    @DisplayName("Should return a valid instance of change password form")
    void shouldReturnValidInstanceOfChangePasswordForm() {
        String password = "Str0ngP@ssword";
        ChangePasswordForm form = new ChangePasswordForm(password);
        assertEquals(password, form.password());
    }
}