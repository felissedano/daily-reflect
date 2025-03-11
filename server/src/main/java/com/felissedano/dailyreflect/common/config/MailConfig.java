package com.felissedano.dailyreflect.common.config;

import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class MailConfig {


    @Bean
    @Profile("mock")
    JavaMailSender javaMailSender() {
        return new MockMailSender();
    }

    private static class MockMailSender implements JavaMailSender {

        @Override
        public MimeMessage createMimeMessage() {
            return new MimeMessage((Session) null);
        }


        @Override
        public MimeMessage createMimeMessage(InputStream contentStream) throws MailException {
            return new MimeMessage((Session) null);
        }

        @Override
        public void send(MimeMessage... mimeMessages) throws MailException {
            for (MimeMessage mimeMessage : mimeMessages) {
                try {
                    System.out.println(STR."\nMimeMessage email -> \nSubject: \{mimeMessage.getSubject()} \nContent : \{mimeMessage.getContent()}\n");
                } catch (MessagingException e) {
                    System.out.println("Some errors happened when try to print mimeMessage");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        @Override
        public void send(SimpleMailMessage... simpleMessages) throws MailException {
            for (SimpleMailMessage simpleMessage : simpleMessages) {
                System.out.println(STR."\nSimpleMailMessage email -> \nSubject: \{simpleMessage.getSubject()} \nContent: \{simpleMessage.getText()}\n");
            }
        }
    }


}

