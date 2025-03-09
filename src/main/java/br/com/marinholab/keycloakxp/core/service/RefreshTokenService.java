package br.com.marinholab.keycloakxp.core.service;

import br.com.marinholab.keycloakxp.core.model.AccessTokenDTO;
import br.com.marinholab.keycloakxp.core.model.common.JwtProperties;
import br.com.marinholab.keycloakxp.core.model.operations.RefreshTokenForm;
import br.com.marinholab.keycloakxp.core.model.properties.KeycloakClientProperties;
import br.com.marinholab.keycloakxp.core.model.properties.KeycloakProperties;
import br.com.marinholab.keycloakxp.exception.RefreshTokenException;
import br.com.marinholab.keycloakxp.external.KeycloakClient;
import feign.FeignException;
import org.keycloak.OAuth2Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class RefreshTokenService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RefreshTokenService.class);
    private final KeycloakProperties keycloakProperties;
    private final KeycloakClient keycloakClient;

    public RefreshTokenService(KeycloakProperties keycloakProperties, KeycloakClient keycloakClient) {
        this.keycloakProperties = keycloakProperties;
        this.keycloakClient = keycloakClient;
    }

    public AccessTokenDTO refreshToken(Jwt jwt, RefreshTokenForm form) throws RefreshTokenException {
        try {
            LOGGER.debug("Initiating request to refresh user access token in Keycloak...");
            String realm = this.keycloakProperties.getRealm();
            KeycloakClientProperties defaultClient = this.keycloakProperties.getDefaultClient();

            return this.keycloakClient.authenticateByGrantType(
                    realm, this.configureRefreshTokenBody(form, defaultClient)
            );
        } catch (FeignException e) {
            LOGGER.error(e.getMessage());
            String username = jwt.getClaimAsString(JwtProperties.PREFERRED_USERNAME);
            HttpStatus httpStatus = HttpStatus.valueOf(e.status());
            throw new RefreshTokenException(httpStatus, username);
        }
    }

    private Map<String, String> configureRefreshTokenBody(RefreshTokenForm form, KeycloakClientProperties defaultClient) {
        return Map.of(
                OAuth2Constants.REFRESH_TOKEN, form.refreshToken(),
                OAuth2Constants.GRANT_TYPE, OAuth2Constants.REFRESH_TOKEN,
                OAuth2Constants.CLIENT_ID, defaultClient.getClientId(),
                OAuth2Constants.CLIENT_SECRET, defaultClient.getClientSecret()
        );
    }
}
