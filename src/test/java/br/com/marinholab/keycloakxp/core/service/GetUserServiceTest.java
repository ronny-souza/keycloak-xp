package br.com.marinholab.keycloakxp.core.service;

import br.com.marinholab.keycloakxp.core.model.UserDTO;
import br.com.marinholab.keycloakxp.core.model.properties.KeycloakProperties;
import br.com.marinholab.keycloakxp.exception.UserNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RealmsResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetUserServiceTest {

    @InjectMocks
    private GetUserService getUserService;

    @Mock
    private Keycloak keycloak;

    @Mock
    private KeycloakProperties keycloakProperties;

    @Test
    @DisplayName("Should throw an exception when the user is not found in the search")
    void shouldThrowAnExceptionWhenTheUserIsNotFoundInTheSearch() {
        RealmsResource realmsResourceAsMock = mock(RealmsResource.class);
        RealmResource realmResourceAsMock = mock(RealmResource.class);
        UsersResource usersResourceAsMock = mock(UsersResource.class);

        when(this.keycloakProperties.getRealm()).thenReturn("realm");
        when(this.keycloak.realms()).thenReturn(realmsResourceAsMock);
        when(realmsResourceAsMock.realm(anyString())).thenReturn(realmResourceAsMock);
        when(realmResourceAsMock.users()).thenReturn(usersResourceAsMock);
        when(usersResourceAsMock.search(anyString(), anyBoolean())).thenReturn(Collections.emptyList());

        assertThrows(
                UserNotFoundException.class,
                () -> this.getUserService.searchUserRepresentationByUsername("root")
        );
    }

    @Test
    @DisplayName("Should return a Keycloak representation of the user if the search is successful")
    void shouldReturnKeycloakRepresentationOfUserIfTheSearchIsSuccessful() throws UserNotFoundException {
        RealmsResource realmsResourceAsMock = mock(RealmsResource.class);
        RealmResource realmResourceAsMock = mock(RealmResource.class);
        UsersResource usersResourceAsMock = mock(UsersResource.class);
        UserRepresentation userRepresentationAsMock = mock(UserRepresentation.class);

        when(this.keycloakProperties.getRealm()).thenReturn("realm");
        when(this.keycloak.realms()).thenReturn(realmsResourceAsMock);
        when(realmsResourceAsMock.realm(anyString())).thenReturn(realmResourceAsMock);
        when(realmResourceAsMock.users()).thenReturn(usersResourceAsMock);
        when(usersResourceAsMock.search(anyString(), anyBoolean())).thenReturn(Collections.singletonList(userRepresentationAsMock));

        UserRepresentation userRepresentation = this.getUserService.searchUserRepresentationByUsername("root");
        assertEquals(userRepresentationAsMock, userRepresentation);
    }

    @Test
    @DisplayName("Should return a user DTO representation of the user if the search is successful")
    void shouldReturnUserDTORepresentationOfUserIfTheSearchIsSuccessful() throws UserNotFoundException {
        RealmsResource realmsResourceAsMock = mock(RealmsResource.class);
        RealmResource realmResourceAsMock = mock(RealmResource.class);
        UsersResource usersResourceAsMock = mock(UsersResource.class);
        UserRepresentation userRepresentationAsMock = mock(UserRepresentation.class);

        when(this.keycloakProperties.getRealm()).thenReturn("realm");
        when(this.keycloak.realms()).thenReturn(realmsResourceAsMock);
        when(realmsResourceAsMock.realm(anyString())).thenReturn(realmResourceAsMock);
        when(realmResourceAsMock.users()).thenReturn(usersResourceAsMock);
        when(usersResourceAsMock.search(anyString(), anyBoolean())).thenReturn(Collections.singletonList(userRepresentationAsMock));
        when(userRepresentationAsMock.getRealmRoles()).thenReturn(Collections.singletonList("USER"));

        UserDTO userDTO = this.getUserService.searchUserAsDTOByUsername("root");
        assertNotNull(userDTO);
    }
}