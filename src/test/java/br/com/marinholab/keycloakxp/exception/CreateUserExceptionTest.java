package br.com.marinholab.keycloakxp.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CreateUserExceptionTest {

    @Test
    @DisplayName("Should return a valid instance of exception with username constructor")
    void shouldReturnValidInstanceOfExceptionWithUsernameConstructor() {
        CreateUserException exception = new CreateUserException("root");
        assertEquals(1, exception.getArgsAsArray().length);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getHttpStatus());
        assertEquals(CreateUserException.DEFAULT_USER_CREATION_FAILURE_MESSAGE, exception.getTranslationCode());
    }

    @Test
    @DisplayName("Should return a valid instance of exception with username and status constructor")
    void shouldReturnValidInstanceOfExceptionWithUsernameAndStatusConstructor() {
        CreateUserException exception = new CreateUserException(HttpStatus.CONFLICT, "root");
        assertEquals(1, exception.getArgsAsArray().length);
        assertEquals(HttpStatus.CONFLICT, exception.getHttpStatus());
        assertEquals(CreateUserException.USER_CREATION_CONFLICT_MESSAGE, exception.getTranslationCode());
    }

    @Test
    @DisplayName("Should return a valid instance of exception with username and status constructor when is not conflict")
    void shouldReturnValidInstanceOfExceptionWithUsernameAndStatusConstructorWhenIsNotConflict() {
        CreateUserException exception = new CreateUserException(HttpStatus.BAD_REQUEST, "root");
        assertEquals(1, exception.getArgsAsArray().length);
        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
        assertEquals(CreateUserException.DEFAULT_USER_CREATION_FAILURE_MESSAGE, exception.getTranslationCode());
    }
}