package br.com.marinholab.keycloakxp.external;

import br.com.marinholab.keycloakxp.core.model.LoginResponseDTO;
import jakarta.ws.rs.core.MediaType;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

@FeignClient(name = "keycloak-client", url = "${keycloak.server}", path = "/realms")
public interface KeycloakClient {

    @PostMapping(
            value = "/{realm}/protocol/openid-connect/token",
            consumes = MediaType.APPLICATION_FORM_URLENCODED,
            produces = MediaType.APPLICATION_JSON
    )
    LoginResponseDTO authenticate(@PathVariable final String realm, Map<String, ?> body);
}
