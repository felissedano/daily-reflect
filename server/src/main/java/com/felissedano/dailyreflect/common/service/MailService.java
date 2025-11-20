package com.felissedano.dailyreflect.common.service;

import jakarta.annotation.Nullable;

public interface MailService {

    boolean sendLocaleTextEmail(String to, String subjectKey, String contentKey, @Nullable Object[] contentArgs);
}
