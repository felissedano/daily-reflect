package com.felissedano.dailyreflect.journaling;

import java.net.URI;
import java.util.Locale;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class JournalExceptionHandler {

    private final MessageSource messageSource;

    public JournalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(exception = JournalNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleAuthException(JournalNotFoundException exception, Locale locale) {
        String detail = messageSource.getMessage("error.journal.journal-not-found", null, locale);
        ProblemDetail pd = ProblemDetail.forStatus(404);
        pd.setTitle("Journal Not Found");
        pd.setDetail(detail);
        pd.setType(URI.create("/problems/journal/journal-not-found"));
        return new ResponseEntity<>(pd, HttpStatusCode.valueOf(404));
    }
}
