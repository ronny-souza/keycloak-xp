package br.com.marinholab.keycloakxp.core.service;

import br.com.marinholab.keycloakxp.core.model.properties.KeycloakProperties;
import br.com.marinholab.keycloakxp.exception.UserNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RealmsResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChangeUserPasswordServiceTest {

    @InjectMocks
    private ChangeUserPasswordService changeUserPasswordService;

    @Mock
    private Keycloak keycloak;

    @Mock
    private KeycloakProperties keycloakProperties;

    @Mock
    private GetUserService getUserService;

    @Test
    @DisplayName("Should call update method and change user password")
    void shouldCallUpdateMethodAndChangeUserPassword() throws UserNotFoundException {
        RealmsResource realmsResourceAsMock = mock(RealmsResource.class);
        RealmResource realmResourceAsMock = mock(RealmResource.class);
        UserRepresentation userRepresentationAsSpy = spy(UserRepresentation.class);
        UsersResource usersResourceAsMock = mock(UsersResource.class);
        UserResource userResourceAsMock = mock(UserResource.class);

        String password = "StrongP@ssword";

        when(this.keycloakProperties.getRealm()).thenReturn("realm");
        when(this.keycloak.realms()).thenReturn(realmsResourceAsMock);
        when(realmsResourceAsMock.realm(anyString())).thenReturn(realmResourceAsMock);
        when(this.getUserService.searchUserRepresentationByUsername(Mockito.any(RealmResource.class), anyString())).thenReturn(userRepresentationAsSpy);
        when(realmResourceAsMock.users()).thenReturn(usersResourceAsMock);
        when(userRepresentationAsSpy.getId()).thenReturn(UUID.randomUUID().toString());
        when(usersResourceAsMock.get(anyString())).thenReturn(userResourceAsMock);

        this.changeUserPasswordService.changePassword("root", password);

        verify(userResourceAsMock).update(userRepresentationAsSpy);
        assertFalse(userRepresentationAsSpy.getCredentials().isEmpty());
        assertTrue(userRepresentationAsSpy.getCredentials().stream().anyMatch(credentialRepresentation -> credentialRepresentation.getValue().equals(password)));
    }
}