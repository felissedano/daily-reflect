package com.felissedano.dailyreflect.auth.exception;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
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
        String detail = messageSource.getMessage("error.auth.link-expired-or-invalid", null, locale);
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
        return new ResponseEntity<>("{\"message\": \"Email already verified or user not exists\"}", HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(value = SamePasswordException.class)
    public ResponseEntity<ProblemDetail> handleSamePasswordException(SamePasswordException exception, Locale locale) {
//        String detail = messageSource.getMessage("error.auth.same-password", null, locale);
        ProblemDetail pd = ProblemDetail.forStatus(400);
        pd.setTitle("New Password Same As Old Password");
        pd.setDetail("N/A");
        pd.setType(URI.create("/problems/auth/same-password"));
        return new ResponseEntity<>(pd, HttpStatus.valueOf(400));
    }

    @ExceptionHandler(value = DisabledException.class)
    public ResponseEntity<ProblemDetail> handleDisabledException(DisabledException exception, Locale locale) {
       ProblemDetail pd = ProblemDetail.forStatus(403);
       pd.setTitle("Account Not Enabled");
       pd.setDetail("This account is not enabled.");
       pd.setType(URI.create("/problems/auth/account-not-enabled"));
       return new ResponseEntity<>(pd, HttpStatus.valueOf(403));

    }

    @ExceptionHandler(value = BadCredentialsException.class)
    public ResponseEntity<ProblemDetail> handleBadCredentialsException(BadCredentialsException exception, Locale locale) {
        String details =  messageSource.getMessage("error.auth.credentials-incorrect", null, locale);
        ProblemDetail pd = ProblemDetail.forStatus(401);
        pd.setTitle("Credentials Incorrect");
        pd.setDetail(details);
        pd.setType(URI.create("/problems/auth/credentials-incorrect"));
        return new ResponseEntity<>(pd, HttpStatus.valueOf(401));
    }

}
