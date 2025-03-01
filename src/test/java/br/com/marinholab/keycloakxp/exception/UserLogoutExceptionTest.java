package br.com.marinholab.keycloakxp.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserLogoutExceptionTest {

    @Test
    @DisplayName("Should return a valid instance of exception with args constructor")
    void shouldReturnValidInstanceOfExceptionWithArgsConstructor() {
        UserLogoutException exception = new UserLogoutException("root");
        assertEquals(1, exception.getArgsAsArray().length);
        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
        assertEquals("auth.logout.failed", exception.getTranslationCode());
    }
}