package br.com.marinholab.keycloakxp.core.service;

import br.com.marinholab.keycloakxp.core.model.AccessTokenDTO;
import br.com.marinholab.keycloakxp.core.model.operations.UserLoginForm;
import br.com.marinholab.keycloakxp.core.model.properties.KeycloakClientProperties;
import br.com.marinholab.keycloakxp.core.model.properties.KeycloakProperties;
import br.com.marinholab.keycloakxp.exception.UserAuthenticationException;
import br.com.marinholab.keycloakxp.external.KeycloakClient;
import feign.FeignException;
import org.keycloak.OAuth2Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class LoginService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginService.class);
    private final KeycloakProperties keycloakProperties;
    private final KeycloakClient keycloakClient;

    public LoginService(KeycloakProperties keycloakProperties, KeycloakClient keycloakClient) {
        this.keycloakProperties = keycloakProperties;
        this.keycloakClient = keycloakClient;
    }

    public AccessTokenDTO login(UserLoginForm form) throws UserAuthenticationException {
        try {
            String realm = this.keycloakProperties.getRealm();
            return this.keycloakClient.authenticateByGrantType(
                    realm, this.configureLoginWithPasswordGrantTypeBody(form)
            );
        } catch (FeignException e) {
            LOGGER.error(e.getMessage(), e);
            throw new UserAuthenticationException(form.username());
        }
    }

    private Map<String, ?> configureLoginWithPasswordGrantTypeBody(UserLoginForm form) {
        KeycloakClientProperties defaultClient = this.keycloakProperties.getDefaultClient();
        return Map.of(
                OAuth2Constants.USERNAME, form.username(),
                OAuth2Constants.PASSWORD, form.password(),
                OAuth2Constants.GRANT_TYPE, OAuth2Constants.PASSWORD,
                OAuth2Constants.CLIENT_ID, defaultClient.getClientId(),
                OAuth2Constants.CLIENT_SECRET, defaultClient.getClientSecret()
        );
    }
}
