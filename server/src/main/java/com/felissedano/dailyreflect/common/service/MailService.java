package com.felissedano.dailyreflect.common.service;

import jakarta.annotation.Nullable;

import java.util.Locale;

public interface MailService {

    boolean sendLocaleTextEmail(String to, String subjectKey, String contentKey, @Nullable Object [] contentArgs);
}
