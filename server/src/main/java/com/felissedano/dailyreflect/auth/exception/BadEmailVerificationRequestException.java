package com.felissedano.dailyreflect.auth.exception;

public class BadEmailVerificationRequestException extends RuntimeException {
    public BadEmailVerificationRequestException(String message) {
        super(message);
    }
}
