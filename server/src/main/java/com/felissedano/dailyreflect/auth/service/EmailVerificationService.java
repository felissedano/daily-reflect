package com.felissedano.dailyreflect.auth.service;

public interface EmailVerificationService {
    boolean sendVerificationEmail(String email, String username, String code);

    boolean resendVerificationEmail(String email);

    void enableUser(String email, String verificationCode);
}
