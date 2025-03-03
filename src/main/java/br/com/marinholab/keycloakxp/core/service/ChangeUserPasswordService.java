package br.com.marinholab.keycloakxp.core.service;

import br.com.marinholab.keycloakxp.core.model.properties.KeycloakProperties;
import br.com.marinholab.keycloakxp.exception.UserNotFoundException;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChangeUserPasswordService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChangeUserPasswordService.class);

    private final Keycloak keycloak;
    private final KeycloakProperties keycloakProperties;
    private final GetUserService getUserService;

    public ChangeUserPasswordService(Keycloak keycloak, KeycloakProperties keycloakProperties, GetUserService getUserService) {
        this.keycloak = keycloak;
        this.keycloakProperties = keycloakProperties;
        this.getUserService = getUserService;
    }

    public void changePassword(String username, String newPassword) throws UserNotFoundException {
        LOGGER.info("Starting password change process for user {}", username);
        String realm = this.keycloakProperties.getRealm();
        RealmResource realmResource = this.keycloak.realms().realm(realm);

        UserRepresentation userRepresentation = this.getUserService.searchUserRepresentationByUsername(realmResource, username);

        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
        credentialRepresentation.setTemporary(false);
        credentialRepresentation.setValue(newPassword);
        userRepresentation.setCredentials(List.of(credentialRepresentation));

        realmResource.users().get(userRepresentation.getId()).update(userRepresentation);
    }
}
