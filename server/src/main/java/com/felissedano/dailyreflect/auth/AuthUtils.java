package com.felissedano.dailyreflect.auth;

import java.util.Date;
import java.util.UUID;

public class AuthUtils {

    public static Date generateTokenExpirationDate() {
        return new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 8);
    }

    public static String generateVerificationToken() {
        return UUID.randomUUID().toString();
    }
}
