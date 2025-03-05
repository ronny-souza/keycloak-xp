package br.com.marinholab.keycloakxp.core.service;

import br.com.marinholab.keycloakxp.core.model.operations.UserLoginForm;
import br.com.marinholab.keycloakxp.core.model.properties.KeycloakClientProperties;
import br.com.marinholab.keycloakxp.core.model.properties.KeycloakProperties;
import br.com.marinholab.keycloakxp.exception.UserAuthenticationException;
import br.com.marinholab.keycloakxp.external.KeycloakClient;
import feign.FeignException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

    @InjectMocks
    private LoginService loginService;

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
        when(this.keycloakClient.authenticateByGrantType(anyString(), anyMap())).thenThrow(FeignException.class);

        assertThrows(
                UserAuthenticationException.class,
                () -> this.loginService.login(formAsMock)
        );
    }
}