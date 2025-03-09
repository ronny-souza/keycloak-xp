package br.com.marinholab.keycloakxp.core.service;


import br.com.marinholab.keycloakxp.core.model.common.JwtProperties;
import br.com.marinholab.keycloakxp.core.model.operations.UserLogoutForm;
import br.com.marinholab.keycloakxp.core.model.properties.KeycloakClientProperties;
import br.com.marinholab.keycloakxp.core.model.properties.KeycloakProperties;
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
public class LogoutService {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogoutService.class);
    private final KeycloakProperties keycloakProperties;
    private final KeycloakClient keycloakClient;

    public LogoutService(KeycloakProperties keycloakProperties,
                         KeycloakClient keycloakClient) {
        this.keycloakProperties = keycloakProperties;
        this.keycloakClient = keycloakClient;
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
                throw new UserLogoutException(jwt.getClaimAsString(JwtProperties.PREFERRED_USERNAME));
            }
        } catch (FeignException e) {
            LOGGER.error(e.getMessage(), e);
            HttpStatus httpStatus = HttpStatus.valueOf(e.status());
            throw new UserLogoutException(httpStatus, jwt.getClaimAsString(JwtProperties.PREFERRED_USERNAME));
        }
    }

    private Map<String, ?> configureLogoutBody(UserLogoutForm form) {
        KeycloakClientProperties defaultClient = this.keycloakProperties.getDefaultClient();
        return Map.of(
                OAuth2Constants.CLIENT_ID, defaultClient.getClientId(),
                OAuth2Constants.CLIENT_SECRET, defaultClient.getClientSecret(),
                OAuth2Constants.REFRESH_TOKEN, form.refreshToken()
        );
    }
}
