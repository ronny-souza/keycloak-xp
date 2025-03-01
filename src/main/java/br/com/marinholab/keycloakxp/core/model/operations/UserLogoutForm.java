package br.com.marinholab.keycloakxp.core.model.operations;

import jakarta.validation.constraints.NotBlank;

public record UserLogoutForm(@NotBlank String refreshToken) {
}
