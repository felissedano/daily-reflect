package com.felissedano.dailyreflect.auth.exception;

public class TokenExpiredOrInvalidException extends RuntimeException {
    public TokenExpiredOrInvalidException(String message) {
        super(message);
    }
}
