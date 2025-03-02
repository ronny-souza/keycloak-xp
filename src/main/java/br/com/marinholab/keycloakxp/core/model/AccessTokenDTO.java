package br.com.marinholab.keycloakxp.core.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

public record AccessTokenDTO(
        @JsonProperty("access_token")
        @Schema(description = "JWT access token, used for authentication in API calls.")
        String accessToken,

        @JsonProperty("expires_in")
        @Schema(description = "Access token expiration time, in seconds.")
        Long expiresIn,

        @JsonProperty("refresh_expires_in")
        @Schema(description = "Refresh token expiration time, in seconds.")
        Long refreshExpiresIn,

        @JsonProperty("refresh_token")
        @Schema(description = "Refresh token, used to obtain a new access token without the need for re-authentication.")
        String refreshToken,

        @JsonProperty("token_type")
        @Schema(description = "Type of token returned, typically 'Bearer'.")
        String tokenType,

        @JsonProperty("session_state")
        @Schema(description = "Session identifier in Keycloak, useful for logout and session tracking.")
        String sessionState,

        @Schema(description = "Scopes assigned to the access token.")
        String scope
) {
}
