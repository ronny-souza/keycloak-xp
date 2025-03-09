package br.com.marinholab.keycloakxp.core.service;

import br.com.marinholab.keycloakxp.core.model.UserDTO;
import br.com.marinholab.keycloakxp.core.model.properties.KeycloakProperties;
import br.com.marinholab.keycloakxp.exception.UserNotFoundException;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class GetUserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetUserService.class);
    private final Keycloak keycloak;
    private final KeycloakProperties keycloakProperties;

    public GetUserService(Keycloak keycloak, KeycloakProperties keycloakProperties) {
        this.keycloak = keycloak;
        this.keycloakProperties = keycloakProperties;
    }

    public UserRepresentation searchUserRepresentationByUsername(RealmResource realmResource, String username) throws UserNotFoundException {
        LOGGER.info("Searching for user {} on Keycloak...", username);
        return realmResource.users().search(username, true)
                .stream()
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    public UserRepresentation searchUserRepresentationByUsername(String username) throws UserNotFoundException {
        String realm = this.keycloakProperties.getRealm();
        RealmResource realmResource = this.keycloak.realms().realm(realm);
        return this.searchUserRepresentationByUsername(realmResource, username);
    }

    public UserDTO searchUserAsDTOByUsername(String username) throws UserNotFoundException {
        return new UserDTO(this.searchUserRepresentationByUsername(username));
    }
}
