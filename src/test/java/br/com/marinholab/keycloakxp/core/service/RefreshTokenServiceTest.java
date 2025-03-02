package br.com.marinholab.keycloakxp.core.service;

import br.com.marinholab.keycloakxp.core.model.operations.RefreshTokenForm;
import br.com.marinholab.keycloakxp.core.model.properties.KeycloakClientProperties;
import br.com.marinholab.keycloakxp.core.model.properties.KeycloakProperties;
import br.com.marinholab.keycloakxp.exception.RefreshTokenException;
import br.com.marinholab.keycloakxp.external.KeycloakClient;
import feign.FeignException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.jwt.Jwt;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RefreshTokenServiceTest {

    @InjectMocks
    private RefreshTokenService refreshTokenService;

    @Mock
    private KeycloakClient keycloakClient;

    @Mock
    private KeycloakProperties keycloakProperties;

    @Test
    @DisplayName("Should throw exception with status 400 when refresh token is not valid")
    void shouldThrowExceptionWithStatus400WhenRefreshTokenIsNotValid() {
        RefreshTokenForm formAsMock = mock(RefreshTokenForm.class);
        Jwt jwtAsMock = mock(Jwt.class);
        FeignException exceptionAsMock = mock(FeignException.class);
        KeycloakClientProperties keycloakClientPropertiesAsMock = mock(KeycloakClientProperties.class);

        when(this.keycloakProperties.getRealm()).thenReturn("realm");
        when(this.keycloakProperties.getDefaultClient()).thenReturn(keycloakClientPropertiesAsMock);
        when(formAsMock.refreshToken()).thenReturn("refreshToken");
        when(keycloakClientPropertiesAsMock.getClientId()).thenReturn("clientId");
        when(keycloakClientPropertiesAsMock.getClientSecret()).thenReturn("clientSecret");
        when(this.keycloakClient.authenticateByGrantType(anyString(), anyMap())).thenThrow(exceptionAsMock);
        when(jwtAsMock.getClaimAsString("preferred_username")).thenReturn("root");
        when(exceptionAsMock.getMessage()).thenReturn("Some exception message");
        when(exceptionAsMock.status()).thenReturn(400);

        assertThrows(RefreshTokenException.class, () -> this.refreshTokenService.refreshToken(jwtAsMock, formAsMock));
    }

}