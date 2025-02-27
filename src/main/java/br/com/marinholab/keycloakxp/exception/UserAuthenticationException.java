package br.com.marinholab.keycloakxp.exception;

import org.springframework.http.HttpStatus;

import java.util.List;

public class UserAuthenticationException extends KeycloakXpException {

    public UserAuthenticationException(String username) {
        super(List.of(username));
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.UNAUTHORIZED;
    }

    @Override
    public String getTranslationCode() {
        return "auth.login.failed";
    }
}
