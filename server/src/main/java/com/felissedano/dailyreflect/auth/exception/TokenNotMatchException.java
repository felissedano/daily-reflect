package com.felissedano.dailyreflect.auth.exception;

public class TokenNotMatchException extends RuntimeException {
    public TokenNotMatchException(String message) {
        super(message);
    }
}
