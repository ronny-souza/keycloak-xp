package br.com.marinholab.keycloakxp.core.model;

import io.swagger.v3.oas.annotations.media.Schema;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.security.oauth2.jwt.Jwt;

@Schema(description = "Represents the response object of creating a user")
public record UserDTO(
        @Schema(description = "User identifier on Keycloak")
        String id,

        @Schema(description = "Unique username")
        String username,

        @Schema(description = "User unique email")
        String email,

        @Schema(description = "User's first name")
        String firstName,

        @Schema(description = "User's last name")
        String lastName,

        @Schema(description = "User role in Keycloak")
        String role
) {

    public UserDTO(UserRepresentation userRepresentation) {
        this(
                userRepresentation.getId(),
                userRepresentation.getUsername(),
                userRepresentation.getEmail(),
                userRepresentation.getFirstName(),
                userRepresentation.getLastName(),
                userRepresentation.getRealmRoles() != null ? userRepresentation.getRealmRoles().getFirst() : "USER"
        );
    }

    public UserDTO(Jwt jwt) {
        this(
                null,
                jwt.getClaimAsString("preferred_username"),
                jwt.getClaimAsString("email"),
                jwt.getClaimAsString("given_name"),
                jwt.getClaimAsString("family_name"),
                null
        );
    }
}
