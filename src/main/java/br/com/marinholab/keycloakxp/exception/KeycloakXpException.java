package br.com.marinholab.keycloakxp.exception;

import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;

public class KeycloakXpException extends Exception {

    private final List<String> args;
    private final String translationCode;
    private final HttpStatus httpStatus;

    public KeycloakXpException() {
        this(Collections.emptyList(), "error.internal", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public KeycloakXpException(List<String> args) {
        this(args, "error.internal", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public KeycloakXpException(List<String> args, String translationCode) {
        this(args, translationCode, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public KeycloakXpException(List<String> args, String translationCode, HttpStatus httpStatus) {
        this.args = args;
        this.translationCode = translationCode;
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }

    public String getTranslationCode() {
        return this.translationCode;
    }

    public Object[] getArgsAsArray() {
        return this.args.toArray();
    }
}
