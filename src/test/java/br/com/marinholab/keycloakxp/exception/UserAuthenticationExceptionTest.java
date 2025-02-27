package br.com.marinholab.keycloakxp.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserAuthenticationExceptionTest {

    @Test
    @DisplayName("Should return a valid instance of exception with args constructor")
    void shouldReturnValidInstanceOfExceptionWithArgsConstructor() {
        UserAuthenticationException exception = new UserAuthenticationException("root");
        assertEquals(1, exception.getArgsAsArray().length);
        assertEquals(HttpStatus.UNAUTHORIZED, exception.getHttpStatus());
        assertEquals("auth.login.failed", exception.getTranslationCode());
    }
}