package br.com.marinholab.keycloakxp.core.model.operations;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Schema(description = "Representation of the payload required for the user authentication operation.")
public record UserLoginForm(@Schema(description = "Unique username")
                            @NotBlank
                            String username,

                            @Schema(description = "User's password")
                            @NotBlank
                            @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$")
                            String password
) {
}
