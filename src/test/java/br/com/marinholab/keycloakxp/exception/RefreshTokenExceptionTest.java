package br.com.marinholab.keycloakxp.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RefreshTokenExceptionTest {

    @Test
    @DisplayName("Should return a valid instance of exception with username constructor")
    void shouldReturnValidInstanceOfExceptionWithUsernameConstructor() {
        RefreshTokenException exception = new RefreshTokenException("root");
        assertEquals(1, exception.getArgsAsArray().length);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getHttpStatus());
        assertEquals(RefreshTokenException.DEFAULT_FAILURE_MESSAGE, exception.getTranslationCode());
    }

    @Test
    @DisplayName("Should return a valid instance of exception with username and status constructor")
    void shouldReturnValidInstanceOfExceptionWithUsernameAndStatusConstructor() {
        RefreshTokenException exception = new RefreshTokenException(HttpStatus.BAD_REQUEST, "root");
        assertEquals(1, exception.getArgsAsArray().length);
        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
        assertEquals(RefreshTokenException.BAD_REQUEST_FAILURE_MESSAGE, exception.getTranslationCode());
    }

    @Test
    @DisplayName("Should return a valid instance of exception with username and status constructor when is not bad request")
    void shouldReturnValidInstanceOfExceptionWithUsernameAndStatusConstructorWhenIsNotBadRequest() {
        RefreshTokenException exception = new RefreshTokenException(HttpStatus.CONFLICT, "root");
        assertEquals(1, exception.getArgsAsArray().length);
        assertEquals(HttpStatus.CONFLICT, exception.getHttpStatus());
        assertEquals(RefreshTokenException.DEFAULT_FAILURE_MESSAGE, exception.getTranslationCode());
    }
}