package com.felissedano.dailyreflect.auth.service;

public interface EmailVerificationService {
    boolean sendVerificationEmail(String email, String username, String code);

    boolean resendVerificationEmail(String email);

    boolean enableUser(String email, String verificationCode);
}
