package br.com.marinholab.keycloakxp.core.model.operations;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Schema(description = "Representation of the payload required for the user access token renewal operation.")
public record RefreshTokenForm(
        @Schema(description = "User refresh token, obtained through authentication in the application.")
        @NotBlank(message = "{user.refresh.token.not.blank}")
        @Pattern(message = "{user.refresh.token.valid.format}", regexp = "([A-Za-z0-9-_]+\\.[A-Za-z0-9-_]+\\.[A-Za-z0-9-_]+)")
        String refreshToken
) {

}
