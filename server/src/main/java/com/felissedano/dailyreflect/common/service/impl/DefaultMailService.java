package com.felissedano.dailyreflect.common.service.impl;

import com.felissedano.dailyreflect.common.service.MailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class DefaultMailService implements MailService {

    private JavaMailSender mailSender;
    private MessageSource messageSource;

    public DefaultMailService(JavaMailSender mailSender, MessageSource messageSource) {
        this.mailSender = mailSender;
        this.messageSource = messageSource;
    }

    @Override
    public boolean sendTextEmail(String to, String subject, String content, Object [] args, Locale locale) {
        String title = messageSource.getMessage(subject, null, locale);
        String message =  messageSource.getMessage(content, args, locale);
        MimeMessage mailMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mailMessage);
        try {
            helper.setSubject(title);
            helper.setText(message);
            helper.setTo(to);
        } catch (MessagingException e) {
            return false;
        }

        mailSender.send(mailMessage);

        return true;
    }

    public boolean sendTextEmail(String to, String subject, String content) {
        return false; //TODO

    }
}
