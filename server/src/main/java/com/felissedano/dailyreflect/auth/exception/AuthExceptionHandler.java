package com.felissedano.dailyreflect.auth.exception;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.util.Locale;

@RestControllerAdvice
public class AuthExceptionHandler {

    private final MessageSource messageSource;

    public AuthExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(value = TokenExpiredOrInvalidException.class)
    public ResponseEntity<ProblemDetail> handleAuthException(TokenExpiredOrInvalidException exception, Locale locale) {
        String detail = messageSource.getMessage("error.auth.token-expired-or-invalid", null, locale);
        ProblemDetail pd = ProblemDetail.forStatus(400);
        pd.setTitle("Email Verification Failed");
        pd.setDetail(detail);
        pd.setType(URI.create("/problems/auth/token-expired-or-invalid"));
        return new ResponseEntity<>(pd, HttpStatusCode.valueOf(400));
    }


    @ExceptionHandler(value = EmailAlreadyVerifiedException.class)
    public ResponseEntity<ProblemDetail> handleAuthException(EmailAlreadyVerifiedException exception, Locale locale) {
        String detail = messageSource.getMessage("error.auth.already-verified", null, locale);
        ProblemDetail pd = ProblemDetail.forStatus(400);
        pd.setTitle("Email Verification Failed");
        pd.setDetail(detail);
        pd.setType(URI.create("/problems/auth/already-verified"));
        return new ResponseEntity<>(pd, HttpStatusCode.valueOf(400));
    }

    @ExceptionHandler(value = BadEmailVerificationRequestException.class)
    public ResponseEntity<String> handleBadEmailVerificationRequestException(BadEmailVerificationRequestException exception, Locale locale) {
        return new ResponseEntity<>("Email already verified or user not exists", HttpStatus.BAD_REQUEST);

    }

}
