package br.com.marinholab.keycloakxp.exception;

import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

public class KeycloakXpException extends Exception {

    private final List<String> args;

    public KeycloakXpException() {
        this.args = new ArrayList<>();
    }
    public KeycloakXpException(List<String> args) {
        this.args = args;
    }

    public HttpStatus getHttpStatus() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public String getTranslationCode() {
        return "error.internal";
    }

    public Object[] getArgsAsArray() {
        return this.args.toArray();
    }
}
