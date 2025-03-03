package br.com.marinholab.keycloakxp.core.model.operations;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ChangePasswordForm(
        @Schema(description = "User's new password. Minimum of 8 characters, with at least one uppercase letter, one lowercase letter, one number and one special character")
        @NotBlank(message = "{user.password.not.blank}")
        @Pattern(message = "{user.password.valid.format}", regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$")
        String password
) {
}
