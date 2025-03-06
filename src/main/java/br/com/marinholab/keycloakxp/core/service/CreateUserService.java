package br.com.marinholab.keycloakxp.core.service;

import br.com.marinholab.keycloakxp.core.model.UserDTO;
import br.com.marinholab.keycloakxp.core.model.operations.CreateUserForm;
import br.com.marinholab.keycloakxp.core.model.properties.KeycloakProperties;
import br.com.marinholab.keycloakxp.exception.CreateUserException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CreateUserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CreateUserService.class);

    private final Keycloak keycloak;
    private final KeycloakProperties keycloakProperties;

    public CreateUserService(Keycloak keycloak, KeycloakProperties keycloakProperties) {
        this.keycloak = keycloak;
        this.keycloakProperties = keycloakProperties;
    }

    public UserDTO createUser(CreateUserForm form) throws CreateUserException {
        LOGGER.info("Starting user creation on Keycloak...");
        RealmResource realm = this.keycloak.realm(this.keycloakProperties.getRealm());

        UserRepresentation userRepresentation = form.configureAsKeycloakUserRepresentation();

        try (Response response = realm.users().create(userRepresentation)) {
            UserResource userResource = realm.users().get(CreatedResponseUtil.getCreatedId(response));
            RoleRepresentation roleResource = realm.roles().get("USER").toRepresentation();

            userResource.roles().realmLevel().add(Collections.singletonList(roleResource));
            return new UserDTO(userResource.toRepresentation());
        } catch (WebApplicationException e) {
            throw new CreateUserException(HttpStatus.valueOf(e.getResponse().getStatus()), form.username());
        }
    }
}
