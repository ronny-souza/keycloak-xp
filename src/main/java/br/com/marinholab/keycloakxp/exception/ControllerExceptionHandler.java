package br.com.marinholab.keycloakxp.exception;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionHandler {

    private final MessageSource messageSource;

    public ControllerExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(CreateUserException.class)
    public ResponseEntity<ProblemDetail> handleCreateUserException(CreateUserException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problemDetail);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleMethodArgumentNotFoundException(MethodArgumentNotValidException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
    }

    @ExceptionHandler(KeycloakXpException.class)
    public ResponseEntity<ProblemDetail> handleKeycloakXpException(KeycloakXpException exception) {
        String translatedMessage = this.messageSource.getMessage(
                exception.getTranslationCode(),
                exception.getArgsAsArray(),
                LocaleContextHolder.getLocale()
        );

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(exception.getHttpStatus(), translatedMessage);
        return ResponseEntity.status(exception.getHttpStatus()).body(problemDetail);
    }
}
