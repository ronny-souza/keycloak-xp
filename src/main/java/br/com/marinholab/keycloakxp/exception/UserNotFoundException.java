package br.com.marinholab.keycloakxp.exception;

import org.springframework.http.HttpStatus;

import java.util.List;

public class UserNotFoundException extends KeycloakXpException {

    public UserNotFoundException(String username) {
        super(List.of(username), "user.not.found", HttpStatus.NOT_FOUND);
    }
}
