package com.felissedano.dailyreflect.common.service.impl;

import com.felissedano.dailyreflect.common.service.MailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.javamail.JavaMailSender;
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
    public boolean sendLocaleTextEmail(String to, String subjectKey, String contentKey, Object [] contentArgs) {
        Locale locale = LocaleContextHolder.getLocale();
        String title = messageSource.getMessage(subjectKey, null, locale);
        String message =  messageSource.getMessage(contentKey, contentArgs, locale);
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
