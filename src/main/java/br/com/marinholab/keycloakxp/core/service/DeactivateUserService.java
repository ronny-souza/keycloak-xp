package br.com.marinholab.keycloakxp.core.service;

import br.com.marinholab.keycloakxp.core.model.properties.KeycloakProperties;
import br.com.marinholab.keycloakxp.exception.UserNotFoundException;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class DeactivateUserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeactivateUserService.class);
    private final Keycloak keycloak;
    private final KeycloakProperties keycloakProperties;
    private final GetUserService getUserService;

    public DeactivateUserService(Keycloak keycloak,
                                 KeycloakProperties keycloakProperties,
                                 GetUserService getUserService) {
        this.keycloak = keycloak;
        this.keycloakProperties = keycloakProperties;
        this.getUserService = getUserService;
    }

    public void deactivate(String username) throws UserNotFoundException {
        LOGGER.info("Starting the user account deactivation process {}", username);
        String realm = this.keycloakProperties.getRealm();
        RealmResource realmResource = this.keycloak.realms().realm(realm);
        UserRepresentation userRepresentation = this.getUserService.searchUserRepresentationByUsername(realmResource, username);
        userRepresentation.setEnabled(false);

        realmResource.users().get(userRepresentation.getId()).update(userRepresentation);
    }
}
