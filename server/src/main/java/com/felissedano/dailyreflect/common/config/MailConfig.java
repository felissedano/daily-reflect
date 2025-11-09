package com.felissedano.dailyreflect.common.config;

import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

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
                    System.out.println("\nMimeMessage email -> \nSubject: " + mimeMessage.getSubject() + "\nContent : "
                            + mimeMessage.getContent() + "\n");
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
                System.out.println("\nSimpleMailMessage email -> \nSubject:" + simpleMessage.getSubject()
                        + "\nContent: " + simpleMessage.getText() + "\n");
            }
        }
    }
}
