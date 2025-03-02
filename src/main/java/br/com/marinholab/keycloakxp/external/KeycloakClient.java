package br.com.marinholab.keycloakxp.external;

import br.com.marinholab.keycloakxp.core.model.AccessTokenDTO;
import br.com.marinholab.keycloakxp.core.model.properties.KeycloakProperties;
import jakarta.ws.rs.core.MediaType;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;

@FeignClient(name = "keycloak-client", url = "${keycloak.server}", path = "/realms")
public interface KeycloakClient {

    /**
     * This endpoint, provided by the Keycloak API, is capable of performing a number of operations.
     * The main ones include user login and token refresh.
     *
     * @param realm The realm of Keycloak environment configured on application properties, which can
     *              be found in {@link KeycloakProperties }
     * @param body  A map representing the form to be sent to Keycloak. The information contained generally
     *              depens on the chosen grant_type.
     * @return An {@link AccessTokenDTO} object containing the access token, refresh token, and information
     * such as the expiration time for both.
     */
    @PostMapping(
            value = "/{realm}/protocol/openid-connect/token",
            consumes = MediaType.APPLICATION_FORM_URLENCODED,
            produces = MediaType.APPLICATION_JSON
    )
    AccessTokenDTO authenticateByGrantType(@PathVariable final String realm, Map<String, ?> body);

    /**
     * This endpoint, provided by the Keycloak API, is capable of destroying the user's session.
     * What actually happens is that the refresh token becomes invalid, so the access token will remain
     * available until it expires, and it will not be possible to renew it.
     *
     * @param realm         The realm of Keycloak environment configured on application properties, which can
     *                      be found in {@link KeycloakProperties }
     * @param authorization A string containing the authenticated user's current access token.
     * @param body          A map representing the form to be sent to Keycloak. The information contained generally
     *                      depens on the chosen grant_type.
     * @return A String containing possible additional information, usually in case of errors.
     */
    @PostMapping(
            value = "/{realm}/protocol/openid-connect/logout",
            consumes = MediaType.APPLICATION_FORM_URLENCODED,
            produces = MediaType.APPLICATION_JSON
    )
    ResponseEntity<String> logout(@PathVariable final String realm,
                                  @RequestHeader("Authorization") String authorization,
                                  Map<String, ?> body);
}
