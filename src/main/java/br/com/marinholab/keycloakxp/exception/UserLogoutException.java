package br.com.marinholab.keycloakxp.exception;

import org.springframework.http.HttpStatus;

import java.util.List;

public class UserLogoutException extends KeycloakXpException {

    public UserLogoutException(String username) {
        super(List.of(username));
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    public String getTranslationCode() {
        return "auth.logout.failed";
    }
}
