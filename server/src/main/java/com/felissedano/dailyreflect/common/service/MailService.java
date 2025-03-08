package com.felissedano.dailyreflect.common.service;

import java.util.Locale;

public interface MailService {
    boolean sendTextEmail(String to, String subject, String content, Locale locale);
}
