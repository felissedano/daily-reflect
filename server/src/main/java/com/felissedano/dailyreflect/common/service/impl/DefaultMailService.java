package com.felissedano.dailyreflect.common.service.impl;

import com.felissedano.dailyreflect.common.service.MailService;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class DefaultMailService implements MailService {

    private JavaMailSender mailSender;

    public DefaultMailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public boolean sendTextEmail(String to, String subject, String content, Locale locale) {
        return false; //TODO
    }

    public boolean sendTextEmail(String to, String subject, String content) {
        return false; //TODO

    }
}
