package com.felissedano.dailyreflect.common.service.impl;

import com.felissedano.dailyreflect.common.service.MailService;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class DefaultMailService implements MailService {

    private final JavaMailSender mailSender;
    private final MessageSource messageSource;

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
            String styleMessage = "<style>body {background-color: white;}</style>" + message;
            helper.setSubject(title);
            helper.setText(styleMessage, true);
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
