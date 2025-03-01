package br.com.marinholab.keycloakxp.core.service;


import br.com.marinholab.keycloakxp.core.model.LoginResponseDTO;
import br.com.marinholab.keycloakxp.core.model.operations.UserLoginForm;
import br.com.marinholab.keycloakxp.core.model.operations.UserLogoutForm;
import br.com.marinholab.keycloakxp.core.model.properties.KeycloakClientProperties;
import br.com.marinholab.keycloakxp.core.model.properties.KeycloakProperties;
import br.com.marinholab.keycloakxp.exception.UserAuthenticationException;
import br.com.marinholab.keycloakxp.exception.UserLogoutException;
import br.com.marinholab.keycloakxp.external.KeycloakClient;
import feign.FeignException;
import org.keycloak.OAuth2Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthenticationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationService.class);
    private final KeycloakProperties keycloakProperties;
    private final KeycloakClient keycloakClient;

    public AuthenticationService(KeycloakProperties keycloakProperties,
                                 KeycloakClient keycloakClient) {
        this.keycloakProperties = keycloakProperties;
        this.keycloakClient = keycloakClient;
    }

    public LoginResponseDTO login(UserLoginForm form) throws UserAuthenticationException {
        try {
            String realm = this.keycloakProperties.getRealm();
            return this.keycloakClient.authenticate(realm, this.configureLoginWithPasswordGrantTypeBody(form));
        } catch (FeignException e) {
            LOGGER.error(e.getMessage(), e);
            throw new UserAuthenticationException(form.username());
        }
    }

    public void logout(UserLogoutForm form, Jwt jwt) throws UserLogoutException {
        try {
            String realm = this.keycloakProperties.getRealm();
            String accessToken = String.format("Bearer %s", jwt.getTokenValue());
            ResponseEntity<String> response = this.keycloakClient.logout(
                    realm, accessToken, this.configureLogoutBody(form)
            );

            if (!response.getStatusCode().equals(HttpStatus.NO_CONTENT)) {
                LOGGER.error("User logout error: {}", response.getBody());
                throw new UserLogoutException(jwt.getClaimAsString("preferred_username"));
            }
        } catch (FeignException e) {
            LOGGER.error("User logout error: {}", e.getMessage());
            throw new UserLogoutException(jwt.getClaimAsString("preferred_username"));
        }
    }

    private Map<String, ?> configureLogoutBody(UserLogoutForm form) {
        KeycloakClientProperties defaultClient = this.keycloakProperties.getDefaultClient();
        return Map.of(
                "client_id", defaultClient.getClientId(),
                "client_secret", defaultClient.getClientSecret(),
                "refresh_token", form.refreshToken()
        );
    }

    private Map<String, ?> configureLoginWithPasswordGrantTypeBody(UserLoginForm form) {
        KeycloakClientProperties defaultClient = this.keycloakProperties.getDefaultClient();
        return Map.of(
                "username", form.username(),
                "password", form.password(),
                OAuth2Constants.GRANT_TYPE, OAuth2Constants.PASSWORD,
                OAuth2Constants.CLIENT_ID, defaultClient.getClientId(),
                OAuth2Constants.CLIENT_SECRET, defaultClient.getClientSecret()
        );
    }
}
