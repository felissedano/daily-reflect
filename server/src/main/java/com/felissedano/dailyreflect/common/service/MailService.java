package com.felissedano.dailyreflect.common.service;

import jakarta.annotation.Nullable;

import java.util.Locale;

public interface MailService {
    boolean sendTextEmail(String to, String subject, String content, @Nullable Object [] args, Locale locale);
}
