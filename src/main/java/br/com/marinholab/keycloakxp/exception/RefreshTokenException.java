package br.com.marinholab.keycloakxp.exception;

import org.springframework.http.HttpStatus;

import java.util.List;

public class RefreshTokenException extends KeycloakXpException {

    public static final String BAD_REQUEST_FAILURE_MESSAGE = "auth.refresh.token.failed.bad.request";
    public static final String DEFAULT_FAILURE_MESSAGE = "auth.refresh.token.failed";

    public RefreshTokenException(String username) {
        super(List.of(username), RefreshTokenException.DEFAULT_FAILURE_MESSAGE);
    }

    public RefreshTokenException(HttpStatus status, String username) {
        super(
                List.of(username),
                status.equals(HttpStatus.BAD_REQUEST) ? BAD_REQUEST_FAILURE_MESSAGE : DEFAULT_FAILURE_MESSAGE,
                status
        );
    }
}
