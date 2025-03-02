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

    @Test
    @DisplayName("Should return a valid instance of exception with args and translation code constructor")
    void shouldReturnValidInstanceOfExceptionWithArgsAndTranslationCodeConstructor() {
        KeycloakXpException exception = new KeycloakXpException(List.of("root"), "translation.code");
        assertEquals(1, exception.getArgsAsArray().length);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getHttpStatus());
        assertEquals("translation.code", exception.getTranslationCode());
    }

    @Test
    @DisplayName("Should return a valid instance of exception with all attributes constructor")
    void shouldReturnValidInstanceOfExceptionWithAllAttributesConstructor() {
        KeycloakXpException exception = new KeycloakXpException(List.of("root"), "translation.code", HttpStatus.BAD_REQUEST);
        assertEquals(1, exception.getArgsAsArray().length);
        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
        assertEquals("translation.code", exception.getTranslationCode());
    }
}