package com.felissedano.dailyreflect.journaling;

public class JournalNotFoundException extends RuntimeException {

    public JournalNotFoundException(String message) {
        super(message);
    }
}
