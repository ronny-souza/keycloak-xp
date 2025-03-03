package br.com.marinholab.keycloakxp.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserNotFoundExceptionTest {

    @Test
    @DisplayName("Should return a valid instance of exception with args constructor")
    void shouldReturnValidInstanceOfExceptionWithArgsConstructor() {
        UserNotFoundException exception = new UserNotFoundException("root");
        assertEquals(1, exception.getArgsAsArray().length);
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
        assertEquals("user.not.found", exception.getTranslationCode());
    }
}