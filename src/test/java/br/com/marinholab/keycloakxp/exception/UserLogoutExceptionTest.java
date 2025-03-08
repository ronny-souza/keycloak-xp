package br.com.marinholab.keycloakxp.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserLogoutExceptionTest {

    @Test
    @DisplayName("Should return a valid instance of exception with username constructor")
    void shouldReturnValidInstanceOfExceptionWithUsernameConstructor() {
        UserLogoutException exception = new UserLogoutException("root");
        assertEquals(1, exception.getArgsAsArray().length);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getHttpStatus());
        assertEquals(UserLogoutException.DEFAULT_FAILURE_MESSAGE, exception.getTranslationCode());
    }

    @Test
    @DisplayName("Should return a valid instance of exception with username and status constructor")
    void shouldReturnValidInstanceOfExceptionWithUsernameAndStatusConstructor() {
        UserLogoutException exception = new UserLogoutException(HttpStatus.BAD_REQUEST, "root");
        assertEquals(1, exception.getArgsAsArray().length);
        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
        assertEquals(UserLogoutException.BAD_REQUEST_FAILURE_MESSAGE, exception.getTranslationCode());
    }

    @Test
    @DisplayName("Should return a valid instance of exception with username and status constructor when is not bad request")
    void shouldReturnValidInstanceOfExceptionWithUsernameAndStatusConstructorWhenIsNotBadRequest() {
        UserLogoutException exception = new UserLogoutException(HttpStatus.CONFLICT, "root");
        assertEquals(1, exception.getArgsAsArray().length);
        assertEquals(HttpStatus.CONFLICT, exception.getHttpStatus());
        assertEquals(UserLogoutException.DEFAULT_FAILURE_MESSAGE, exception.getTranslationCode());
    }
}