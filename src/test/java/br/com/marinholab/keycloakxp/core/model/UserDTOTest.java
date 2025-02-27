package br.com.marinholab.keycloakxp.core.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserDTOTest {

    @Test
    @DisplayName("Should return a valid instance of UserDTO")
    void shouldReturnValidInstanceOfUserDTO() {
        String id = "id";
        String username = "username";
        String email = "email";
        String firstName = "firstName";
        String lastName = "lastName";
        String role = "role";

        UserDTO userDTO = new UserDTO(
                id,
                username,
                email,
                firstName,
                lastName,
                role
        );

        assertNotNull(userDTO);
        assertEquals(id, userDTO.id());
        assertEquals(username, userDTO.username());
        assertEquals(email, userDTO.email());
        assertEquals(firstName, userDTO.firstName());
        assertEquals(lastName, userDTO.lastName());
        assertEquals(role, userDTO.role());
    }

    @Test
    @DisplayName("Should return a valid instance of UserDTO with user representation constructor")
    void shouldReturnValidInstanceOfUserDTOWithUserRepresentationConstructor() {
        String id = "id";
        String username = "username";
        String email = "email";
        String firstName = "firstName";
        String lastName = "lastName";
        String role = "USER";

        UserRepresentation userRepresentationAsMock = mock(UserRepresentation.class);
        when(userRepresentationAsMock.getId()).thenReturn(id);
        when(userRepresentationAsMock.getUsername()).thenReturn(username);
        when(userRepresentationAsMock.getEmail()).thenReturn(email);
        when(userRepresentationAsMock.getFirstName()).thenReturn(firstName);
        when(userRepresentationAsMock.getLastName()).thenReturn(lastName);
        when(userRepresentationAsMock.getRealmRoles()).thenReturn(List.of(role));

        UserDTO userDTO = new UserDTO(userRepresentationAsMock);

        assertNotNull(userDTO);
        assertEquals(id, userDTO.id());
        assertEquals(username, userDTO.username());
        assertEquals(email, userDTO.email());
        assertEquals(firstName, userDTO.firstName());
        assertEquals(lastName, userDTO.lastName());
        assertEquals(role, userDTO.role());
    }

    @Test
    @DisplayName("Should return a valid instance of UserDTO with JWT constructor")
    void shouldReturnValidInstanceOfUserDTOWithJWTConstructor() {
        String username = "username";
        String email = "email";
        String firstName = "firstName";
        String lastName = "lastName";

        Jwt jwtAsMock = mock(Jwt.class);
        when(jwtAsMock.getClaimAsString("preferred_username")).thenReturn(username);
        when(jwtAsMock.getClaimAsString("email")).thenReturn(email);
        when(jwtAsMock.getClaimAsString("given_name")).thenReturn(firstName);
        when(jwtAsMock.getClaimAsString("family_name")).thenReturn(lastName);

        UserDTO userDTO = new UserDTO(jwtAsMock);

        assertNotNull(userDTO);
        assertEquals(username, userDTO.username());
        assertEquals(email, userDTO.email());
        assertEquals(firstName, userDTO.firstName());
        assertEquals(lastName, userDTO.lastName());
    }
}