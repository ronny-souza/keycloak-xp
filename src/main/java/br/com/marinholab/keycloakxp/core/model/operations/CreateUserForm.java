package br.com.marinholab.keycloakxp.core.model.operations;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.List;

@Schema(description = "Representation of the payload required to create a user in Keycloak.")
public record CreateUserForm(
        @Schema(description = "Unique username")
        @NotBlank(message = "{user.username.not.blank}")
        String username,

        @Schema(description = "User unique email")
        @NotBlank(message = "{user.email.not.blank}")
        @Email(message = "{user.email.valid.format}")
        String email,

        @Schema(description = "User's first name")
        @NotBlank(message = "{user.firstname.not.blank}")
        String firstName,

        @Schema(description = "User's last name")
        @NotBlank(message = "{user.lastname.not.blank}")
        String lastName,

        @Schema(description = "User's password. Minimum of 8 characters, with at least one uppercase letter, one lowercase letter, one number and one special character")
        @NotBlank(message = "{user.password.not.blank}")
        @Pattern(message = "{user.password.valid.format}", regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$")
        String password
) {

    public UserRepresentation configureAsKeycloakUserRepresentation() {
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(this.username());
        userRepresentation.setEmail(this.email());
        userRepresentation.setFirstName(this.firstName());
        userRepresentation.setLastName(this.lastName());
        userRepresentation.setEnabled(true);

        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setPriority(Integer.MAX_VALUE);
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
        credentialRepresentation.setTemporary(false);
        credentialRepresentation.setValue(this.password());
        userRepresentation.setCredentials(List.of(credentialRepresentation));
        return userRepresentation;
    }
}
