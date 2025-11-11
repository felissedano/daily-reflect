package com.felissedano.dailyreflect.common.exception;

import java.net.URI;
import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CommonExceptionHandler{

    private final MessageSource messageSource;

    public CommonExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }


    @ExceptionHandler(exception = MailException.class)
    public ResponseEntity<ProblemDetail> handleMailException(MailException exception, Locale locale) {
        ProblemDetail pd = ProblemDetail.forStatus(500);
        pd.setTitle("Server Error");
        pd.setDetail("Something went wrong on the server");
        pd.setType(URI.create("/problems/server/server-error"));
        return new ResponseEntity<>(pd, HttpStatusCode.valueOf(500));
    }

    
}
