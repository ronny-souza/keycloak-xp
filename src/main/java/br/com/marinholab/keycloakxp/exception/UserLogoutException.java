package br.com.marinholab.keycloakxp.exception;

import org.springframework.http.HttpStatus;

import java.util.List;

public class UserLogoutException extends KeycloakXpException {

    public static final String DEFAULT_FAILURE_MESSAGE = "auth.logout.failed";
    public static final String BAD_REQUEST_FAILURE_MESSAGE = "auth.logout.failed.bad.request";

    public UserLogoutException(String username) {
        super(List.of(username), UserLogoutException.DEFAULT_FAILURE_MESSAGE);
    }

    public UserLogoutException(HttpStatus status, String username) {
        super(
                List.of(username),
                status.equals(HttpStatus.BAD_REQUEST) ? BAD_REQUEST_FAILURE_MESSAGE : DEFAULT_FAILURE_MESSAGE,
                status
        );
    }
}
