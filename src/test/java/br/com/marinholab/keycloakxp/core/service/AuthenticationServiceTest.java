package br.com.marinholab.keycloakxp.core.service;

import br.com.marinholab.keycloakxp.core.model.operations.UserLoginForm;
import br.com.marinholab.keycloakxp.core.model.operations.UserLogoutForm;
import br.com.marinholab.keycloakxp.core.model.properties.KeycloakClientProperties;
import br.com.marinholab.keycloakxp.core.model.properties.KeycloakProperties;
import br.com.marinholab.keycloakxp.exception.UserAuthenticationException;
import br.com.marinholab.keycloakxp.exception.UserLogoutException;
import br.com.marinholab.keycloakxp.external.KeycloakClient;
import feign.FeignException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @InjectMocks
    private AuthenticationService authenticationService;

    @Mock
    private KeycloakProperties keycloakProperties;

    @Mock
    private KeycloakClient keycloakClient;

    @Test
    @DisplayName("Should throw exception when authentication on Keycloak fails")
    void shouldThrowExceptionWhenAuthenticationOnKeycloakFails() {
        String username = "user";
        String password = "Str0ngP@ssword";
        UserLoginForm formAsMock = mock(UserLoginForm.class);
        KeycloakClientProperties keycloakClientPropertiesAsMock = mock(KeycloakClientProperties.class);

        when(formAsMock.username()).thenReturn(username);
        when(formAsMock.password()).thenReturn(password);
        when(this.keycloakProperties.getRealm()).thenReturn("realm");
        when(this.keycloakProperties.getDefaultClient()).thenReturn(keycloakClientPropertiesAsMock);
        when(keycloakClientPropertiesAsMock.getClientId()).thenReturn("clientId");
        when(keycloakClientPropertiesAsMock.getClientSecret()).thenReturn("secret");
        when(this.keycloakClient.authenticate(anyString(), anyMap())).thenThrow(FeignException.class);

        assertThrows(
                UserAuthenticationException.class,
                () -> this.authenticationService.login(formAsMock)
        );
    }

    @Test
    @DisplayName("Should throw exception when unexpected error occurs in Keycloak logout request")
    void shouldThrowExceptionWhenUnexpectedErrorOccursInKeycloakLogoutRequest() {
        String refreshToken = "refreshToken";
        UserLogoutForm formAsMock = mock(UserLogoutForm.class);
        KeycloakClientProperties keycloakClientPropertiesAsMock = mock(KeycloakClientProperties.class);
        Jwt jwtAsMock = mock(Jwt.class);

        when(formAsMock.refreshToken()).thenReturn(refreshToken);
        when(this.keycloakProperties.getRealm()).thenReturn("realm");
        when(jwtAsMock.getTokenValue()).thenReturn("tokenValue");
        when(this.keycloakProperties.getDefaultClient()).thenReturn(keycloakClientPropertiesAsMock);
        when(keycloakClientPropertiesAsMock.getClientId()).thenReturn("clientId");
        when(keycloakClientPropertiesAsMock.getClientSecret()).thenReturn("secret");
        when(this.keycloakClient.logout(anyString(), anyString(), anyMap())).thenThrow(FeignException.class);
        when(jwtAsMock.getClaimAsString("preferred_username")).thenReturn("root");

        assertThrows(
                UserLogoutException.class,
                () -> this.authenticationService.logout(formAsMock, jwtAsMock)
        );
    }

    @Test
    @DisplayName("Should throw exception when status on logout response from keycloak is not 204")
    void shouldThrowExceptionWhenStatusOnLogoutResponseFromKeycloakIsNot204() {
        String refreshToken = "refreshToken";
        UserLogoutForm formAsMock = mock(UserLogoutForm.class);
        KeycloakClientProperties keycloakClientPropertiesAsMock = mock(KeycloakClientProperties.class);
        Jwt jwtAsMock = mock(Jwt.class);
        ResponseEntity<String> response = ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad request");

        when(formAsMock.refreshToken()).thenReturn(refreshToken);
        when(this.keycloakProperties.getRealm()).thenReturn("realm");
        when(jwtAsMock.getTokenValue()).thenReturn("tokenValue");
        when(this.keycloakProperties.getDefaultClient()).thenReturn(keycloakClientPropertiesAsMock);
        when(keycloakClientPropertiesAsMock.getClientId()).thenReturn("clientId");
        when(keycloakClientPropertiesAsMock.getClientSecret()).thenReturn("secret");
        when(this.keycloakClient.logout(anyString(), anyString(), anyMap())).thenReturn(response);
        when(jwtAsMock.getClaimAsString("preferred_username")).thenReturn("root");

        assertThrows(
                UserLogoutException.class,
                () -> this.authenticationService.logout(formAsMock, jwtAsMock)
        );
    }

    @Test
    @DisplayName("Should do nothing when logout successfully occurs in Keycloak")
    void shouldDoNothingWhenLogoutSuccessfullyOccursInKeycloak() {
        String refreshToken = "refreshToken";
        UserLogoutForm formAsMock = mock(UserLogoutForm.class);
        KeycloakClientProperties keycloakClientPropertiesAsMock = mock(KeycloakClientProperties.class);
        Jwt jwtAsMock = mock(Jwt.class);
        ResponseEntity<String> response = ResponseEntity.status(HttpStatus.NO_CONTENT).body("Success!");

        when(formAsMock.refreshToken()).thenReturn(refreshToken);
        when(this.keycloakProperties.getRealm()).thenReturn("realm");
        when(jwtAsMock.getTokenValue()).thenReturn("tokenValue");
        when(this.keycloakProperties.getDefaultClient()).thenReturn(keycloakClientPropertiesAsMock);
        when(keycloakClientPropertiesAsMock.getClientId()).thenReturn("clientId");
        when(keycloakClientPropertiesAsMock.getClientSecret()).thenReturn("secret");
        when(this.keycloakClient.logout(anyString(), anyString(), anyMap())).thenReturn(response);

        assertDoesNotThrow(() -> this.authenticationService.logout(formAsMock, jwtAsMock));
    }

}