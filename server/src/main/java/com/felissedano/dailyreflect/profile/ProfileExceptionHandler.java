package com.felissedano.dailyreflect.profile;

import java.net.URI;
import java.util.Locale;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ProfileExceptionHandler {
    private final MessageSource messageSource;

    public ProfileExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(value = ProfileNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleAuthException(ProfileNotFoundException exception, Locale locale) {
        String detail = messageSource.getMessage("error.profile.profile-not-found", null, locale);
        ProblemDetail pd = ProblemDetail.forStatus(500);
        pd.setTitle("Profile Not Found");
        pd.setDetail(detail);
        pd.setType(URI.create("/problems/profile/profile-not-found"));
        return new ResponseEntity<>(pd, HttpStatusCode.valueOf(500));
    }
}
