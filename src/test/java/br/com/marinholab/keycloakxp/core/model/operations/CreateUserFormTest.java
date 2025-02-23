package br.com.marinholab.keycloakxp.core.model.operations;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.keycloak.representations.idm.UserRepresentation;

import static org.junit.jupiter.api.Assertions.*;

class CreateUserFormTest {

    @Test
    @DisplayName("Should return a valid instance of create user form")
    void shouldReturnValidInstanceOfCreateUserForm() {
        String username = "username";
        String email = "email";
        String firstName = "firstName";
        String lastName = "lastName";
        String password = "Str0ngP@ssword";

        CreateUserForm form = new CreateUserForm(
                username,
                email,
                firstName,
                lastName,
                password
        );

        assertNotNull(form);
        assertEquals(username, form.username());
        assertEquals(email, form.email());
        assertEquals(firstName, form.firstName());
        assertEquals(lastName, form.lastName());
        assertEquals(password, form.password());
    }

    @Test
    @DisplayName("Should return a valid instance of keycloak UserRepresentation")
    void shouldReturnValidInstanceOfKeycloakUserRepresentation() {
        String username = "username";
        String email = "email";
        String firstName = "firstName";
        String lastName = "lastName";
        String password = "Str0ngP@ssword";

        CreateUserForm form = new CreateUserForm(
                username,
                email,
                firstName,
                lastName,
                password
        );

        UserRepresentation userRepresentation = form.configureAsKeycloakUserRepresentation();

        assertNotNull(userRepresentation);
        assertEquals(username, userRepresentation.getUsername());
        assertEquals(email, userRepresentation.getEmail());
        assertEquals(firstName, userRepresentation.getFirstName());
        assertEquals(lastName, userRepresentation.getLastName());
        assertTrue(userRepresentation.isEnabled());
        assertFalse(userRepresentation.getCredentials().isEmpty());
        assertTrue(userRepresentation.getCredentials().stream().anyMatch(credentialRepresentation -> credentialRepresentation.getValue().equals(form.password())));
    }
}