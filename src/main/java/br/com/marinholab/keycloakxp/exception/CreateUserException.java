package br.com.marinholab.keycloakxp.exception;

import org.springframework.http.HttpStatus;

import java.util.List;

public class CreateUserException extends KeycloakXpException {

    public static final String USER_CREATION_CONFLICT_MESSAGE = "user.creation.conflict";
    public static final String DEFAULT_USER_CREATION_FAILURE_MESSAGE = "user.creation.failed";

    public CreateUserException(String username) {
        super(List.of(username), DEFAULT_USER_CREATION_FAILURE_MESSAGE);
    }

    public CreateUserException(HttpStatus status, String username) {
        super(
                List.of(username),
                status.equals(HttpStatus.CONFLICT) ? USER_CREATION_CONFLICT_MESSAGE : DEFAULT_USER_CREATION_FAILURE_MESSAGE,
                status
        );
    }
}
