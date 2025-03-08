package com.felissedano.dailyreflect.auth.exception;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class AuthExceptionHandler {

    @ExceptionHandler(value = TokenExpiredException.class)
    public ResponseEntity<ProblemDetail> handleAuthException(TokenExpiredException exception) {
        ProblemDetail pd = ProblemDetail.forStatus(400);
        pd.setTitle("Token Expired");
        return new ResponseEntity<>(pd, HttpStatusCode.valueOf(404));
    }

    @ExceptionHandler(value = TokenNotMatchException.class)
    public ResponseEntity<ProblemDetail> handleAuthException(TokenNotMatchException exception) {
        ProblemDetail pd = ProblemDetail.forStatus(400);
        pd.setTitle("Token Not Match");
        return new ResponseEntity<>(pd, HttpStatusCode.valueOf(404));
    }

    @ExceptionHandler(value = AlreadyVerifiedException.class)
    public ResponseEntity<ProblemDetail> handleAuthException(AlreadyVerifiedException exception) {
        ProblemDetail pd = ProblemDetail.forStatus(400);
        pd.setTitle("Already Verified");
        return new ResponseEntity<>(pd, HttpStatusCode.valueOf(404));
    }
}
