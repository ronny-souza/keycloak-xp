package br.com.marinholab.keycloakxp.exception;

public class CreateUserException extends Exception {

    public CreateUserException(Exception e) {
        super(e.getMessage());
    }
}
