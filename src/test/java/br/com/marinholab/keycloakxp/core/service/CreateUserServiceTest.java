package br.com.marinholab.keycloakxp.core.service;

import br.com.marinholab.keycloakxp.core.model.operations.CreateUserForm;
import br.com.marinholab.keycloakxp.core.model.properties.KeycloakProperties;
import br.com.marinholab.keycloakxp.exception.CreateUserException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.*;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URI;
import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateUserServiceTest {

    @InjectMocks
    private CreateUserService createUserService;

    @Mock
    private Keycloak keycloak;

    @Mock
    private KeycloakProperties keycloakProperties;

    @Test
    @DisplayName("Should throw exception when keycloak response is not 201 when creating user")
    void shouldThrowExceptionWhenKeycloakResponseIsNot201WhenCreatingUser() {
        CreateUserForm createUserFormAsMock = mock(CreateUserForm.class);
        RealmResource realmResourceAsMock = mock(RealmResource.class);
        UserRepresentation userRepresentationAsMock = mock(UserRepresentation.class);
        UsersResource usersResourceAsMock = mock(UsersResource.class);
        WebApplicationException webApplicationExceptionAsMock = mock(WebApplicationException.class);
        Response responseAsMock = mock(Response.class);


        when(this.keycloakProperties.getRealm()).thenReturn("realm");
        when(this.keycloak.realm(this.keycloakProperties.getRealm())).thenReturn(realmResourceAsMock);
        when(createUserFormAsMock.configureAsKeycloakUserRepresentation()).thenReturn(userRepresentationAsMock);
        when(realmResourceAsMock.users()).thenReturn(usersResourceAsMock);
        when(usersResourceAsMock.create(userRepresentationAsMock)).thenThrow(webApplicationExceptionAsMock);
        when(webApplicationExceptionAsMock.getResponse()).thenReturn(responseAsMock);
        when(responseAsMock.getStatus()).thenReturn(409);
        when(createUserFormAsMock.username()).thenReturn("root");

        assertThrows(
                CreateUserException.class,
                () -> this.createUserService.createUser(createUserFormAsMock)
        );

        responseAsMock.close();
    }

    @Test
    @DisplayName("Should create a new user on Keycloak")
    void shouldCreateNewUserOnKeycloak() throws CreateUserException {
        String id = UUID.randomUUID().toString();
        String username = "username";
        String email = "email";
        String firstName = "firstName";
        String lastName = "lastName";
        String role = "USER";

        CreateUserForm createUserFormAsMock = mock(CreateUserForm.class);
        RealmResource realmResourceAsMock = mock(RealmResource.class);
        UserRepresentation userRepresentationAsMock = mock(UserRepresentation.class);
        UsersResource usersResourceAsMock = mock(UsersResource.class);
        UserResource userResourceAsMock = mock(UserResource.class);
        RolesResource rolesResourceAsMock = mock(RolesResource.class);
        RoleResource roleResourceAsMock = mock(RoleResource.class);
        RoleRepresentation roleRepresentationAsMock = mock(RoleRepresentation.class);
        RoleMappingResource roleMappingResourceAsMock = mock(RoleMappingResource.class);
        RoleScopeResource roleScopeResourceAsMock = mock(RoleScopeResource.class);
        UserRepresentation createdUserRepresentationAsMock = mock(UserRepresentation.class);


        when(this.keycloakProperties.getRealm()).thenReturn("realm");
        when(this.keycloak.realm(this.keycloakProperties.getRealm())).thenReturn(realmResourceAsMock);
        when(createUserFormAsMock.configureAsKeycloakUserRepresentation()).thenReturn(userRepresentationAsMock);
        when(realmResourceAsMock.users()).thenReturn(usersResourceAsMock);

        try (Response responseAsMock = mock(Response.class)) {
            when(usersResourceAsMock.create(userRepresentationAsMock)).thenReturn(responseAsMock);
            when(responseAsMock.getStatusInfo()).thenReturn(Response.Status.CREATED);
            when(responseAsMock.getLocation()).thenReturn(URI.create(String.format("/user/%s", id)));
            when(usersResourceAsMock.get(anyString())).thenReturn(userResourceAsMock);
            when(realmResourceAsMock.roles()).thenReturn(rolesResourceAsMock);
            when(rolesResourceAsMock.get(anyString())).thenReturn(roleResourceAsMock);
            when(roleResourceAsMock.toRepresentation()).thenReturn(roleRepresentationAsMock);
            when(userResourceAsMock.roles()).thenReturn(roleMappingResourceAsMock);
            when(roleMappingResourceAsMock.realmLevel()).thenReturn(roleScopeResourceAsMock);
        }

        when(userResourceAsMock.toRepresentation()).thenReturn(createdUserRepresentationAsMock);
        when(createdUserRepresentationAsMock.getId()).thenReturn(id);
        when(createdUserRepresentationAsMock.getUsername()).thenReturn(username);
        when(createdUserRepresentationAsMock.getEmail()).thenReturn(email);
        when(createdUserRepresentationAsMock.getFirstName()).thenReturn(firstName);
        when(createdUserRepresentationAsMock.getLastName()).thenReturn(lastName);
        when(createdUserRepresentationAsMock.getRealmRoles()).thenReturn(Collections.singletonList(role));

        this.createUserService.createUser(createUserFormAsMock);
        verify(roleScopeResourceAsMock).add(anyList());
    }
}