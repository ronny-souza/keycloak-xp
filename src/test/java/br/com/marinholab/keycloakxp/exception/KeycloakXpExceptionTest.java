package br.com.marinholab.keycloakxp.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class KeycloakXpExceptionTest {

    @Test
    @DisplayName("Should return a valid instance of exception with no args constructor")
    void shouldReturnValidInstanceOfExceptionWithNoArgsConstructor() {
        KeycloakXpException exception = new KeycloakXpException();
        assertEquals(0, exception.getArgsAsArray().length);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getHttpStatus());
        assertEquals("error.internal", exception.getTranslationCode());
    }

    @Test
    @DisplayName("Should return a valid instance of exception with args constructor")
    void shouldReturnValidInstanceOfExceptionWithArgsConstructor() {
        KeycloakXpException exception = new KeycloakXpException(List.of("root"));
        assertEquals(1, exception.getArgsAsArray().length);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getHttpStatus());
        assertEquals("error.internal", exception.getTranslationCode());
    }
}